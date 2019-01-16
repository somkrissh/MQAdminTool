/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MQApi.Connection;

import java.util.Properties;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;

import MQApi.Models.Query.ConnectionDetailModel;

/**
 *
 * @author jzhou
 */
public class MQConnection {
	@SuppressWarnings("unchecked")
	public static MQQueueManager GetMQQueueManager(ConnectionDetailModel connectionDetail) throws MQException {
		MQQueueManager queueManager = null;
		try {
			String userId = System.getProperty("user.name");
			Properties properties = new Properties();
			properties.put(MQConstants.HOST_NAME_PROPERTY, connectionDetail.Host);
			properties.put(MQConstants.PORT_PROPERTY, Integer.parseInt(connectionDetail.Port));
			properties.put(MQConstants.CHANNEL_PROPERTY, connectionDetail.Channel);
			properties.put("transport", "MQSeries");

			/** Below are the code snippets added to handle SSL with MQ-9.x - starts here **/
			
			properties.put(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY, false);

			MQEnvironment.hostname = connectionDetail.Host;
			MQEnvironment.channel = connectionDetail.Channel;
			MQEnvironment.port = Integer.parseInt(connectionDetail.Port);
			MQEnvironment.sslFipsRequired = false;
			MQEnvironment.properties.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES_CLIENT);
			MQEnvironment.properties.put(MQConstants.SSL_CIPHER_SUITE_PROPERTY, "TLS_RSA_WITH_AES_256_CBC_SHA256");
			
			/** Below are the code snippets added to handle SSL with MQ-9.x - ends here **/

			// properties.put(MQConstants.APPNAME_PROPERTY, "MQ Admin Tool");
			
			if (connectionDetail.User != null && !connectionDetail.User.isEmpty()) {
				properties.put(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY, true);
				properties.put(MQConstants.USER_ID_PROPERTY, connectionDetail.User);
				properties.put(MQConstants.PASSWORD_PROPERTY, connectionDetail.Password);
			} else {
				properties.put(MQConstants.USER_ID_PROPERTY, userId);
			}
			try {
				queueManager = new MQQueueManager(connectionDetail.QueueManager, properties);
			} catch (MQException ex) {
				properties.put("securityExit", new SecurityExit(null, null)); // for MQ ver < 7
				queueManager = new MQQueueManager(connectionDetail.QueueManager, properties);
			}
			MQQueue commandQueue = queueManager.accessQueue(queueManager.getCommandInputQueueName(), CMQC.MQOO_INQUIRE);
			if (commandQueue.getOpenInputCount() == 0) {
				throw new MQException(2, 1, "Command server is not running on this queue manager");
			}
			return queueManager;
		} catch (MQException ex) {
			if (queueManager != null) {
				queueManager.disconnect();
				queueManager.close();
			}
			throw ex;
		}
	}
}
