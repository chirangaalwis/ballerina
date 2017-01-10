/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.ballerina.core.utils;

import org.wso2.ballerina.core.message.BallerinaMessageDataSource;
import org.wso2.ballerina.core.message.StringDataSource;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.DefaultCarbonMessage;
import org.wso2.carbon.messaging.Header;

import java.util.List;

/**
 * Util Class contains method for generating a message
 *
 * @since 1.0.0
 */
public class MessageUtils {

    public CarbonMessage generateRawMessage() {
        return new DefaultCarbonMessage();
    }

    public CarbonMessage generateHTTPMessage(String path, String method) {
        return generateHTTPMessage(path, method, null, null);
    }

    public CarbonMessage generateHTTPMessage(String path, String method, BallerinaMessageDataSource payload) {
        return generateHTTPMessage(path, method, null, payload);
    }

    public CarbonMessage generateHTTPMessage(String path, String method, String payload) {
        return generateHTTPMessage(path, method, null, new StringDataSource(payload));
    }


    public CarbonMessage generateHTTPMessage(String path, String method, List<Header> headers,
                                             BallerinaMessageDataSource payload) {

        CarbonMessage carbonMessage = new DefaultCarbonMessage();

        // Set meta data
        carbonMessage.setProperty(org.wso2.carbon.messaging.Constants.PROTOCOL,
                                  org.wso2.ballerina.core.nativeimpl.connectors.http.Constants.PROTOCOL_HTTP);
        carbonMessage.setProperty(org.wso2.carbon.messaging.Constants.LISTENER_INTERFACE_ID,
                                  org.wso2.ballerina.core.nativeimpl.connectors.http.Constants.DEFAULT_INTERFACE);
        // Set url
        carbonMessage.setProperty(org.wso2.carbon.messaging.Constants.TO, path);

        // Set Headers
        if (headers != null) {
            carbonMessage.setHeaders(headers);
        }

        // Set message body
        if (payload != null) {
            payload.setOutputStream(carbonMessage.getOutputStream());
            carbonMessage.setMessageDataSource(payload);
            carbonMessage.setAlreadyRead(true);
        }

        return carbonMessage;
    }



}