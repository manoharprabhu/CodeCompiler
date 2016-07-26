var request = require("request");
var chai = require("chai");
var assert = chai.assert;
var expect = chai.expect;
var ENDPOINT_BASE = "http://localhost:8080/codecompiler";
var SUBMIT_PROGRAM_URL = ENDPOINT_BASE + "/submit";
var CHECK_STATUS_URL = ENDPOINT_BASE + "/status";
var PING_URL = ENDPOINT_BASE + "/ping";

var checkEndpointService = function(callback) {
    request.get({url: PING_URL}, function(err, httpResponse, body){
	if(err) {
            callback(false);
	}
        if(body === "pong") {
	    callback(true);
        } else {
            callback(false);
	}
    });
};

var submitProgram = function(program, input, timeout, language, callback) {
    var data = {
	program: program,
	input: input,
	timeout: timeout,
	language: language
    };
    request.post({url: SUBMIT_PROGRAM_URL, form: data}, function(err, httpResponse, body){
        if(err) {
            callback(err);
	} else {
	    callback(null, JSON.parse(body).data.queueId);
	}
    });
};

var checkStatusAfterTime = function(time, queueId, callback) {
setTimeout(function(){
    request.get({url: CHECK_STATUS_URL + "?queueId=" + queueId}, function(err,httpResponse,body){
        if(err) {
	    callback(err);
	} else {
	    body = JSON.parse(body);
	    callback(null, body.data.errorMessage, body.data.output, body.data.programStatus);
	}
    });
}, time * 1000);
};

before("Check if the endpoint service is running", function(done){
    checkEndpointService(function(isRunning){
        if(!isRunning) {
	    throw new Error("Endpoint service is not running. Aborting all tests");
	}
	done();
    });
});

describe("System test for codecompiler", function(){
	this.timeout(10000);
	describe("C program tests", function(){
		describe("Submit a successfully running C program", function() {
		    it("C program should run successfully and produce 'success' as output and status 6", function(done){
			    submitProgram("#include<stdio.h>\r\nint main() {printf(\"success\");return 0;}", "", 1, "c", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(3,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    expect(output).to.equal("success");
					}
					done()
				    });
				}
			    });	
		    });
		});

		describe("Submit a wrong C program", function() {
		    it("C program should fail compilation and produce 3 as status", function(done){
			    submitProgram("#include<stdio.h>\r\nint main() {f(\"success\")return 0;}", "", 1, "c", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(3,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    assert.equal(output, null, "Output should be null");
					    assert.equal(programStatus, 3, "Program should not compile successfully");
					}
					done();
				    });
				}
			    });	
		    });
		});


		describe("Submit a long running C program", function() {
		    it("C program should fail to run in 1 second, and the status should be set to 4", function(done){
			    submitProgram("#include<stdio.h>\r\nint main() { while(1) printf(\"success\");return 0;}", "", 1, "c", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(5,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    assert.equal(output, null, "Output should be null");
					    assert.equal(programStatus, 4, "Program should timeout");
					}
					done();
				    });
				}
			    });	
		    });
		});

		describe("Submit a C program the returns non-zero exit status", function() {
		    it("C program should return non-zero exit status and program status should be set to 5", function(done){
			    submitProgram("#include<stdio.h>\r\nint main() {printf(\"success\");return 4;}", "", 1, "c", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(3,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    assert.equal(output, null, "Output should be null");
					    assert.equal(programStatus, 5, "Program should not return 0");
					}
					done();
				    });
				}
			    });	
		    });
		});

		describe("Submit a crashing C program", function() {
		    it("C program should crash and the program status should be set to 5", function(done){
			    submitProgram("#include<stdio.h>\r\nint main() {printf(\"success %d\", 1/0);return 0;}", "", 1, "c", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(3,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    assert.equal(output, null, "Output should be null");
					    assert.equal(programStatus, 5, "Program should crash");
					}
					done();
				    });
				}
			    });	
		    });
		});

		describe("Submit a C program that does not accept input, but you supply input anyway", function() {
		    it("C program should run properly and return status 6", function(done){
			    submitProgram("#include<stdio.h>\r\nint main() {printf(\"success\");return 0;}", "4 6", 1, "c", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(3,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    assert.equal(output, "success", "Output should be success");
					    assert.equal(programStatus, 6, "Program should run successfully");
					}
					done();
				    });
				}
			    });	
		    });
		});
		
		describe("Submit a C program that accepts input and produces output based on the input", function() {
		    it("C program should run properly and return status 6", function(done){
			    submitProgram("#include<stdio.h>\r\nint main() {int a,b; scanf(\"%d%d\",&a,&b); printf(\"%d\",a*b);return 0;}", "4 6", 1, "c", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(3,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    assert.equal(output, "24", "Output should be success");
					    assert.equal(programStatus, 6, "Program should run successfully");
					}
					done();
				    });
				}
			    });	
		    });
		});
	});

	describe("Javascript tests", function(){
		describe("Submit a successfully running Javascript program", function() {
		    it("Javascript program should run successfully and produce 'success' as output and status 6", function(done){
			    submitProgram("console.log(\"success\")", "", 1, "js", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(3,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    expect(output).to.equal("success\n");
					}
					done()
				    });
				}
			    });	
		    });
		});
		describe("Submit a wrong Javascript program", function() {
		    it("JS program should crash on runtime", function(done){
			    submitProgram("\"use strict\"; nonExistentObject.run();", "", 1, "js", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(3,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    assert.equal(output, null, "Output should be null");
					    assert.equal(programStatus, 5, "Program should not run successfully");
					}
					done();
				    });
				}
			    });	
		    });
		});
		describe("Submit a long running Javascript program", function() {
		    it("Javascript program should fail to run in 1 second, and the status should be set to 4", function(done){
			    submitProgram("while(true) { console.log(\"Doomed\");}", "", 1, "js", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(5,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    assert.equal(output, null, "Output should be null");
					    assert.equal(programStatus, 4, "Program should timeout");
					}
					done();
				    });
				}
			    });	
		    });
		}); 
		describe("Submit a Javascript program that does not accept input, but you supply input anyway", function() {
		    it("Javascript program should run properly and return status 6", function(done){
			    submitProgram("var a=5; console.log(a);", "4 6", 1, "js", function(err, queueId){
				if(!err) {
				    checkStatusAfterTime(3,queueId, function(err, errorMessage, output, programStatus){
					if(!err) {
					    assert.equal(output, "5\n", "Output should be 5");
					    assert.equal(programStatus, 6, "Program should run successfully");
					}
					done();
				    });
				}
			    });	
		    });
		});
	});
	
});

