package com.hengyi.cas.webflow.provider;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.configurer.AbstractCasWebflowConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.ActionState;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

import static org.apereo.cas.web.flow.CasWebflowConstants.TRANSITION_ID_SEND_TICKET_GRANTING_TICKET;
import static org.apereo.cas.web.flow.CasWebflowConstants.TRANSITION_ID_SUCCESS;

/**
 * @author jzb 2018-01-30
 */
public class HyWebflowConfigurer extends AbstractCasWebflowConfigurer {
    public HyWebflowConfigurer(FlowBuilderServices flowBuilderServices, FlowDefinitionRegistry loginFlowDefinitionRegistry, ApplicationContext applicationContext, CasConfigurationProperties casProperties) {
        super(flowBuilderServices, loginFlowDefinitionRegistry, applicationContext, casProperties);
    }

    @Override
    protected void doInitialize() {
        Flow flow = this.getLoginFlow();
        if (flow != null) {
            ActionState actionState = createActionState(flow, "hyWebflowAuthenticationCheck", createEvaluateAction("hyWebflowAuthenticationAction"));
            actionState.getTransitionSet().add(createTransition(TRANSITION_ID_SUCCESS, TRANSITION_ID_SEND_TICKET_GRANTING_TICKET));
            createStateDefaultTransition(actionState, this.getStartState(flow).getId());
            setStartState(flow, actionState);
        }
    }
}
