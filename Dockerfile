############ Image ############
FROM openjdk:17-alpine

############ Definition Arguments Variable on Dockerfile ############
ARG BUILD_PATH="./build"
ARG ARTIFACT_PATH="libs"
ARG APPLICATION_PATH="app"
ARG JAR_FILE=".jar"

############ Definition Environment Variable on Container ############
ENV TZ=Asia/Bangkok
ENV APPLICATION_PATH=${APPLICATION_PATH}
ENV JAR_FILE=${JAR_FILE}
ENV PROFILE_ACTIVE=do

############ Make Directory ############
RUN mkdir -p --verbose ${APPLICATION_PATH}
RUN mkdir -p --verbose ${APPLICATION_PATH}/${SH_SCRIPT_PATH}

############ Copy Files ############
COPY ${BUILD_PATH}/${ARTIFACT_PATH}/${JAR_FILE} ${APPLICATION_PATH}/${JAR_FILE}
RUN chmod +rx ${APPLICATION_PATH}/${JAR_FILE}

############ Startup Command ############
WORKDIR ${APPLICATION_PATH}
CMD ["sh", "-c", "java -Dspring.profiles.active=${PROFILE_ACTIVE} \
-Dspring.datasource.url=${DBHOST} \
-Dspring.datasource.username=${DBUSER} \
-Dspring.datasource.password=${DBPASS} \
-Dinfrastructure.services.mq.downstream.queueDistributeOrderMDC=${MHT_MQDEST} \
-Dibm.mq.queueManager=${MHT_MQMANAGER} \
-Dibm.mq.channel=${MHT_MQCHANNEL} \
-Dibm.mq.connName=${MHT_MQHOST} \
-Dinfrastructure.services.mq.downstream.queueDistributeOrderWCT=${WCT_MQDEST} \
-Dibm.mq.queueManagerWCT=${WCT_MQMANAGER} \
-Dibm.mq.channelWCT=${WCT_MQCHANNEL} \
-Dibm.mq.connNameWCT=${WCT_MQHOST} \
-Dibm.mq.user=${MQUSER} \
-Dibm.mq.password=${MQPASS} \
-jar ${JAR_FILE}"]