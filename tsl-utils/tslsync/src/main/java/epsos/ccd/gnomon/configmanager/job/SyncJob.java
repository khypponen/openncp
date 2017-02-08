/***Licensed to the Apache Software Foundation (ASF) under one
 *or more contributor license agreements.  See the NOTICE file
 *distributed with this work for additional information
 *regarding copyright ownership.  The ASF licenses this file
 *to you under the Apache License, Version 2.0 (the
 *"License"); you may not use this file except in compliance
 *with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 **//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package epsos.ccd.gnomon.configmanager.job;

import epsos.ccd.gnomon.configmanager.TSLSynchronizer;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.cert.CertificateException;

public class SyncJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(SyncJob.class);

    /**
     *
     * @param context
     * @throws JobExecutionException
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            TSLSynchronizer.main(null);
        } catch (ParserConfigurationException ex) {
            logger.error(null, ex);
        } catch (SAXException ex) {
            logger.error(null, ex);
        } catch (IOException ex) {
            logger.error(null, ex);
        } catch (CertificateException ex) {
            logger.error(null, ex);
        } catch (Exception ex) {
            logger.error(null, ex);
        }
    }
}
