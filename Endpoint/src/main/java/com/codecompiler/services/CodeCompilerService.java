package com.codecompiler.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.codecompiler.configuration.RabbitMQConfiguration;
import com.codecompiler.vo.ProgramEntity;
import com.codecompiler.vo.ProgramStatusResponse;
import com.codecompiler.vo.ProgramSubmitResponse;
import com.codecompiler.vo.RecentSubmissions;
import com.codecompiler.vo.Response;
import com.codecompiler.vo.Submission;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
@Service
public class CodeCompilerService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private ProgramRepository programRepository;
	private RabbitTemplate rabbitTemplate;

	@Autowired
	public CodeCompilerService(ProgramRepository programRepository, RabbitTemplate rabbitTemplate) {
		this.programRepository = programRepository;
		this.rabbitTemplate = rabbitTemplate;
	}

	public Response<ProgramSubmitResponse> submitProgram(String program, String input, int timeout, String language) {
		if (!isLanguageValid(language))
			return new Response<ProgramSubmitResponse>(null);
		ProgramEntity programEntity = new ProgramEntity();
		ProgramSubmitResponse response = null;
		String uniqueId = generateUniqueID();
		programEntity.setProgram(program);
		programEntity.setInput(input);
		programEntity.setExecutionTimeLimit(timeout);
		programEntity.setQueuedTime(new Date());
		programEntity.setQueueId(uniqueId);
		programEntity.setLanguage(language);
		try {
			programRepository.save(programEntity);
			rabbitTemplate.convertAndSend(RabbitMQConfiguration.QUEUE_NAME, uniqueId);
			response = new ProgramSubmitResponse(uniqueId);
		} catch (DataAccessResourceFailureException e) {
			logger.error("Database seems to be down: ", e);
		} catch (AmqpException e) {
			logger.error("Messaging queue seems to be down: ", e);
		}
		return new Response<ProgramSubmitResponse>(response);
	}

	public Response<ProgramStatusResponse> checkProgramStatus(String queueId) {
		ProgramEntity programEntity = null;
		try {
			programEntity = programRepository.findByQueueId(queueId);
		} catch (DataAccessResourceFailureException e) {
			logger.error("Database seems to be down: ", e);
		}
		ProgramStatusResponse response;
		if (programEntity == null) {
			response = new ProgramStatusResponse(ProgramStatusResponse.PROGRAM_NOT_FOUND, null, "Program not found",
					null);
		} else {
			response = new ProgramStatusResponse(programEntity.getProgramStatus(), programEntity.getOutput(),
					programEntity.getErrorMessage(), programEntity.getQueuedTime());
		}
		return new Response<ProgramStatusResponse>(response);
	}

	public static boolean isLanguageValid(String language) {
		return "c".equals(language) || "js".equals(language);
	}

	public String generateUniqueID() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString();
	}

	public Response getRecentSubmissions(int pageNumber, int rowSize) {
		final PageRequest page = new PageRequest(pageNumber, rowSize, new Sort(new Sort.Order(Direction.DESC, "queuedTime")));
		Page<ProgramEntity> programEntities = programRepository.findAll(page);
		List<ProgramEntity> programEntitiesList = programEntities.getContent();
		List<Submission> submissions = new ArrayList<Submission>();
		RecentSubmissions recentSubmissions = new RecentSubmissions(submissions,
				programEntities.hasPrevious() ? pageNumber - 1 : -1,
				programEntities.hasNext() ? pageNumber + 1 : -1, programEntitiesList.size());
		for (ProgramEntity entity : programEntitiesList) {
			Submission submission = new Submission(entity.getQueueId(), entity.getQueuedTime(), entity.getProgramStatus());
			submissions.add(submission);
		}
		return new Response<RecentSubmissions>(recentSubmissions);
	}

}
