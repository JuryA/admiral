/*
 * Copyright (c) 2017 VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with separate copyright notices
 * and license terms. Your use of these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package com.vmware.admiral.auth.idm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.vmware.admiral.auth.AuthBaseTest;
import com.vmware.admiral.auth.project.ProjectFactoryService;
import com.vmware.admiral.auth.project.ProjectService.ProjectState;
import com.vmware.admiral.common.test.CommonTestStateFactory;
import com.vmware.admiral.service.common.RegistryService;
import com.vmware.admiral.service.common.RegistryService.RegistryState;
import com.vmware.admiral.service.common.SslTrustCertificateService;
import com.vmware.admiral.service.common.SslTrustCertificateService.SslTrustCertificateState;
import com.vmware.xenon.common.ServiceDocument;
import com.vmware.xenon.common.ServiceDocumentQueryResult;
import com.vmware.xenon.common.UriUtils;
import com.vmware.xenon.services.common.AuthCredentialsService;
import com.vmware.xenon.services.common.AuthCredentialsService.AuthCredentialsServiceState;

public class RoleRestrictionsTest extends AuthBaseTest {
    public static final String EXPECTED_ILLEGAL_ACCESS_ERROR_MESSAGE = "Should've thrown IllegalAccessError!";
    public static final String FORBIDDEN = "forbidden";
    public static final String FIRST_CERTIFICATE_PATH = "test_ssl_trust.PEM";
    public static final String SECOND_CERTIFICATE_PATH = "test_ssl_trust2.PEM";

    @Test
    public void testClaudAdminHasAccessToCredentials() throws Throwable {
        host.assumeIdentity(buildUserServicePath(USER_EMAIL_ADMIN));

        AuthCredentialsServiceState cred = new AuthCredentialsServiceState();
        cred.userEmail = "test";

        // POST
        AuthCredentialsServiceState createdState = doPost(cred, AuthCredentialsService.FACTORY_LINK);
        assertNotNull(createdState);
        assertNotNull(createdState.documentSelfLink);

        // GET
        AuthCredentialsServiceState retrievedState = getDocument(AuthCredentialsServiceState.class, createdState.documentSelfLink);
        assertNotNull(retrievedState);

        // PUT
        createdState.userEmail = "updated-name";
        AuthCredentialsServiceState updatedState = doPut(createdState);
        assertNotNull(updatedState);
        assertTrue(createdState.userEmail.equals(updatedState.userEmail));

        // DELETE
        doDelete(UriUtils.buildUri(host, createdState.documentSelfLink), false);
        retrievedState = getDocumentNoWait(AuthCredentialsServiceState.class, createdState.documentSelfLink);
        assertNull(retrievedState);
    }

    @Test
    public void testClaudAdminHasAccessToCertificates() throws Throwable {
        host.assumeIdentity(buildUserServicePath(USER_EMAIL_ADMIN));

        SslTrustCertificateState cert = new SslTrustCertificateState();
        cert.certificate = CommonTestStateFactory.getFileContent(FIRST_CERTIFICATE_PATH).trim();

        // POST
        SslTrustCertificateState createdState = doPost(cert, SslTrustCertificateService.FACTORY_LINK);
        assertNotNull(createdState);
        assertNotNull(createdState.documentSelfLink);

        // GET
        SslTrustCertificateState retrievedState = getDocument(SslTrustCertificateState.class, createdState.documentSelfLink);
        assertNotNull(retrievedState);

        // PUT
        createdState.certificate = CommonTestStateFactory.getFileContent(SECOND_CERTIFICATE_PATH).trim();
        SslTrustCertificateState updatedState = doPut(createdState);
        assertNotNull(updatedState);
        assertTrue(createdState.certificate.equals(updatedState.certificate));

        // DELETE
        doDelete(UriUtils.buildUri(host, createdState.documentSelfLink), false);
        retrievedState = getDocumentNoWait(SslTrustCertificateState.class, createdState.documentSelfLink);
        assertNull(retrievedState);
    }

    @Test
    public void testClaudAdminHasAccessToRegistries() throws Throwable {
        host.assumeIdentity(buildUserServicePath(USER_EMAIL_ADMIN));

        RegistryState registry = new RegistryState();
        registry.name = "test";

        // POST
        RegistryState createdState = doPost(registry, RegistryService.FACTORY_LINK);
        assertNotNull(createdState);
        assertNotNull(createdState.documentSelfLink);

        // GET
        RegistryState retrievedState = getDocument(RegistryState.class, createdState.documentSelfLink);
        assertNotNull(retrievedState);

        // PUT
        createdState.name = "updated-name";
        RegistryState updatedState = doPut(createdState);
        assertNotNull(updatedState);
        assertTrue(createdState.name.equals(updatedState.name));

        // DELETE
        doDelete(UriUtils.buildUri(host, createdState.documentSelfLink), false);
        retrievedState = getDocumentNoWait(RegistryState.class, createdState.documentSelfLink);
        assertNull(retrievedState);
    }

    @Test
    public void testClaudAdminHasAccessToProjects() throws Throwable {
        host.assumeIdentity(buildUserServicePath(USER_EMAIL_ADMIN));

        ProjectState project = new ProjectState();
        project.name = "test";

        // POST
        ProjectState createdState = doPost(project, ProjectFactoryService.SELF_LINK);
        assertNotNull(createdState);
        assertNotNull(createdState.documentSelfLink);

        // GET
        ProjectState retrievedState = getDocument(ProjectState.class, createdState.documentSelfLink);
        assertNotNull(retrievedState);

        // PUT
        createdState.name = "updated-name";
        ProjectState updatedState = doPut(createdState);
        assertNotNull(updatedState);
        assertTrue(createdState.name.equals(updatedState.name));

        // DELETE
        doDelete(UriUtils.buildUri(host, createdState.documentSelfLink), false);
        retrievedState = getDocumentNoWait(ProjectState.class, createdState.documentSelfLink);
        assertNull(retrievedState);
    }

    @Test
    public void testClaudAdminHasAccessToConfiguration() throws Throwable {
        // TODO: WIP
    }

    @Test
    public void testClaudAdminHasAccessToLogs() throws Throwable {
        // TODO: WIP
    }

    @Test
    public void testBasicUserRestrictionsToCredentials() throws Throwable {

        AuthCredentialsServiceState cred = new AuthCredentialsServiceState();
        cred.userEmail = "test";

        // GET
        host.assumeIdentity(buildUserServicePath(USER_EMAIL_ADMIN));
        AuthCredentialsServiceState createdState = doPost(cred, AuthCredentialsService.FACTORY_LINK);
        assertNotNull(createdState);
        assertNotNull(createdState.documentSelfLink);

        host.assumeIdentity(buildUserServicePath(USER_EMAIL_BASIC_USER));
        doGetWithRestrictionVerification(createdState, AuthCredentialsService.FACTORY_LINK, AuthCredentialsServiceState.class.getName());

        // POST
        doPostWithRestrictionVerification(cred, AuthCredentialsService.FACTORY_LINK);

        // PUT
        createdState.userEmail = "updated-name";
        doPutWithRestrictionVerification(createdState, AuthCredentialsService.FACTORY_LINK);

        // DELETE
        doDeleteWithRestrictionVerification(createdState, AuthCredentialsService.FACTORY_LINK);
    }

    @Test
    public void testBasicUserRestrictionsToCertificates() throws Throwable {

        SslTrustCertificateState cert = new SslTrustCertificateState();
        cert.certificate = CommonTestStateFactory.getFileContent(FIRST_CERTIFICATE_PATH).trim();

        // GET
        host.assumeIdentity(buildUserServicePath(USER_EMAIL_ADMIN));
        SslTrustCertificateState createdState = doPost(cert, SslTrustCertificateService.FACTORY_LINK);
        assertNotNull(createdState);
        assertNotNull(createdState.documentSelfLink);

        host.assumeIdentity(buildUserServicePath(USER_EMAIL_BASIC_USER));
        doGetWithRestrictionVerification(createdState, SslTrustCertificateService.FACTORY_LINK, SslTrustCertificateState.class.getName());

        // POST
        doPostWithRestrictionVerification(cert, SslTrustCertificateService.FACTORY_LINK);

        // PUT
        createdState.commonName = "updated-name";
        doPutWithRestrictionVerification(createdState, SslTrustCertificateService.FACTORY_LINK);

        // DELETE
        doDeleteWithRestrictionVerification(createdState, SslTrustCertificateService.FACTORY_LINK);
    }


    @Test
    public void testBasicUserRestrictionsToRegistries() throws Throwable {

        RegistryState registry = new RegistryState();
        registry.name = "test";

        // GET
        host.assumeIdentity(buildUserServicePath(USER_EMAIL_ADMIN));
        RegistryState createdState = doPost(registry, RegistryService.FACTORY_LINK);
        assertNotNull(createdState);
        assertNotNull(createdState.documentSelfLink);

        host.assumeIdentity(buildUserServicePath(USER_EMAIL_BASIC_USER));
        doGetWithRestrictionVerification(createdState, RegistryService.FACTORY_LINK, RegistryState.class.getName());

        // POST
        doPostWithRestrictionVerification(registry, RegistryService.FACTORY_LINK);

        // PUT
        createdState.name = "updated-name";
        doPutWithRestrictionVerification(createdState, RegistryService.FACTORY_LINK);

        // DELETE
        doDeleteWithRestrictionVerification(createdState, RegistryService.FACTORY_LINK);
    }

    @Test
    public void testBasicUserRestrictionsToProjects() throws Throwable {

        ProjectState project = new ProjectState();
        project.name = "test";

        host.assumeIdentity(buildUserServicePath(USER_EMAIL_ADMIN));
        ProjectState createdState = doPost(project, ProjectFactoryService.SELF_LINK);
        assertNotNull(createdState);
        assertNotNull(createdState.documentSelfLink);

        host.assumeIdentity(buildUserServicePath(USER_EMAIL_BASIC_USER));

        // POST
        doPostWithRestrictionVerification(project, ProjectFactoryService.SELF_LINK);

        // PUT
        createdState.name = "updated-name";
        doPutWithRestrictionVerification(createdState, ProjectFactoryService.SELF_LINK);

        // DELETE
        doDeleteWithRestrictionVerification(createdState, ProjectFactoryService.SELF_LINK);
    }

    @Test
    public void testBasicUserRestrictionsToConfiguration() throws Throwable {
        // TODO: WIP
    }

    @Test
    public void testBasicUserRestrictionsToLogs() throws Throwable {
        // TODO: WIP
    }

    private void assertForbiddenMessage(IllegalAccessError e) {
        assertTrue(e.getMessage().toLowerCase().startsWith(FORBIDDEN));
    }

    private void doPostWithRestrictionVerification(ServiceDocument doc, String selfLink) throws Throwable {
        host.log("POST to %s", selfLink);

        try {
            doPost(doc, selfLink);
            fail(EXPECTED_ILLEGAL_ACCESS_ERROR_MESSAGE);
        } catch (IllegalAccessError e) {
            assertForbiddenMessage(e);
        }
    }

    private void doGetWithRestrictionVerification(ServiceDocument createdState, String selfLink, String className) throws Throwable {
        host.log("GET to %s", selfLink);

        // Verify basic user cannot list the documents
        List<String> docs = getDocument(
                ServiceDocumentQueryResult.class, selfLink)
                .documentLinks;
        assertTrue(docs == null || docs.isEmpty());

        try {
            getDocument(Class.forName(className), createdState.documentSelfLink);
            fail(EXPECTED_ILLEGAL_ACCESS_ERROR_MESSAGE);
        } catch (IllegalAccessError e) {
            assertForbiddenMessage(e);
        }
    }

    private void doPutWithRestrictionVerification(ServiceDocument doc, String selfLink) throws Throwable {
        host.log("PUT to %s", selfLink);

        try {
            doPut(doc);
            fail(EXPECTED_ILLEGAL_ACCESS_ERROR_MESSAGE);
        } catch (IllegalAccessError e) {
            assertForbiddenMessage(e);
        }
    }


    private void doDeleteWithRestrictionVerification(ServiceDocument doc, String selfLink) throws Throwable {
        host.log("DELETE to %s", selfLink);

        try {
            doDelete(UriUtils.buildUri(host, doc.documentSelfLink), false);
            fail(EXPECTED_ILLEGAL_ACCESS_ERROR_MESSAGE);
        } catch (IllegalAccessError e) {
            assertForbiddenMessage(e);
        }
    }
}
