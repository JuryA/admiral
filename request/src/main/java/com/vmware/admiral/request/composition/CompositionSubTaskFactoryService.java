/*
 * Copyright (c) 2016 VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with separate copyright notices
 * and license terms. Your use of these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package com.vmware.admiral.request.composition;

import com.vmware.admiral.common.ManagementUriParts;
import com.vmware.admiral.request.composition.CompositionSubTaskService.CompositionSubTaskState;
import com.vmware.admiral.service.common.AbstractSecuredFactoryService;
import com.vmware.xenon.common.Service;

/**
 * Factory service implementing {@link AbstractSecuredFactoryService} used to create instances of
 * {@link CompositionSubTaskService}.
 */
public class CompositionSubTaskFactoryService extends AbstractSecuredFactoryService {
    public static final String SELF_LINK = ManagementUriParts.REQUEST_COMPOSITION_SUB_TASK;

    public CompositionSubTaskFactoryService() {
        super(CompositionSubTaskState.class);
        super.toggleOption(ServiceOption.PERSISTENCE, true);
        super.toggleOption(ServiceOption.REPLICATION, true);
    }

    @Override
    public Service createServiceInstance() throws Throwable {
        return new CompositionSubTaskService();
    }
}
