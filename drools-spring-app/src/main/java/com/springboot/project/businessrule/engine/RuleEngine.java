package com.springboot.project.businessrule.engine;

import com.springboot.project.common.exception.BadRequestException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Component;

/** Compiles DRL source into a ready-to-execute stateless Drools session. */
@Component
public class RuleEngine {

    /**
     * @throws BadRequestException when the DRL has compilation errors, listing the Drools messages
     */
    public StatelessKieSession newSession(String drl, String packageName) {
        KieHelper kieHelper = new KieHelper();
        Resource drlResource = ResourceFactory.newByteArrayResource(drl.getBytes(StandardCharsets.UTF_8));
        drlResource.setTargetPath(packageName);
        kieHelper.addResource(drlResource, ResourceType.DRL);
        rejectCompilationErrors(kieHelper.verify());
        return kieHelper.build().newStatelessKieSession();
    }

    private static void rejectCompilationErrors(Results results) {
        if (!results.hasMessages(Message.Level.ERROR)) {
            return;
        }
        String errors =
                results.getMessages(Message.Level.ERROR).stream()
                        .map(Message::getText)
                        .collect(Collectors.joining("; "));
        throw new BadRequestException("Business rule does not compile: " + errors);
    }
}
