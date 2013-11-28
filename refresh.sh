# fetches drone strike data from http://api.dronestre.am/data
# and generates the json files that app clients will request

DRONE_BACKEND_PATH=`dirname "$0"`

JAR_FILE=$DRONE_BACKEND_PATH/target/drone-backend-0.1.0-SNAPSHOT-standalone.jar
SAVE_PATH=$DRONE_BACKEND_PATH/cache

java -jar $JAR_FILE $SAVE_PATH
