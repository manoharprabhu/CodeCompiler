#~/bin/bash
CONTAINERS=$(docker ps -a -q)
if [ -z "$CONTAINERS" ]
then
	echo "No containers running"; exit 0;
fi
STOP=$(docker stop $(docker ps -a -q))
REMOVE=$(docker rm $(docker ps -a -q))
echo "Stopped and removed all containers"
