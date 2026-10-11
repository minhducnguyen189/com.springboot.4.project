package com.springboot.project.businessrule.drl;

import com.springboot.project.businessrule.model.BusinessRuleDefinitionModel;
import com.springboot.project.businessrule.model.ConflictModel;
import com.springboot.project.businessrule.model.GlobalModel;
import com.springboot.project.businessrule.model.PatternModel;
import com.springboot.project.businessrule.model.RuleModel;
import java.util.List;
import java.util.stream.Collectors;
import org.drools.drl.ast.descr.AndDescr;
import org.drools.drl.ast.descr.ExprConstraintDescr;
import org.drools.drl.ast.descr.GlobalDescr;
import org.drools.drl.ast.descr.ImportDescr;
import org.drools.drl.ast.descr.PackageDescr;
import org.drools.drl.ast.descr.PatternDescr;
import org.drools.drl.ast.descr.RuleDescr;
import org.drools.mvel.DrlDumper;
import org.springframework.stereotype.Component;

/** Renders a {@link BusinessRuleDefinitionModel} as DRL source text. */
@Component
public class DrlBuilder {

    private static final String STATEMENT_SEPARATOR = ";";

    public String build(BusinessRuleDefinitionModel definition) {
        PackageDescr packageDescr = new PackageDescr(definition.name());
        orEmpty(definition.imports()).forEach(target -> packageDescr.addImport(toImport(target)));
        orEmpty(definition.globals()).forEach(global -> packageDescr.addGlobal(toGlobal(global)));
        for (RuleModel rule : definition.rules()) {
            packageDescr.addRule(toRule(rule));
            orEmpty(rule.conflicts())
                    .forEach(conflict -> packageDescr.addRule(toConflictRule(conflict)));
        }
        return new DrlDumper().dump(packageDescr);
    }

    private static ImportDescr toImport(String target) {
        ImportDescr importDescr = new ImportDescr();
        importDescr.setTarget(target);
        return importDescr;
    }

    private static GlobalDescr toGlobal(GlobalModel global) {
        return new GlobalDescr(global.identifier(), global.type());
    }

    private static RuleDescr toRule(RuleModel rule) {
        AndDescr lhs = new AndDescr();
        rule.when().forEach(pattern -> lhs.addDescr(toPattern(pattern)));
        List<String> actions =
                rule.then().stream()
                        .flatMap(consequence -> orEmpty(consequence.actions()).stream())
                        .toList();
        return toRuleDescr(rule.rule(), lhs, actions);
    }

    private static RuleDescr toConflictRule(ConflictModel conflict) {
        AndDescr lhs = new AndDescr();
        lhs.addDescr(
                toPattern(
                        new PatternModel(
                                conflict.identifier(), conflict.type(), conflict.conditions())));
        return toRuleDescr(conflict.name(), lhs, conflict.actions());
    }

    private static RuleDescr toRuleDescr(String name, AndDescr lhs, List<String> actions) {
        RuleDescr ruleDescr = new RuleDescr(name);
        ruleDescr.setLhs(lhs);
        ruleDescr.setConsequence(toConsequence(actions));
        return ruleDescr;
    }

    private static PatternDescr toPattern(PatternModel pattern) {
        PatternDescr patternDescr = new PatternDescr(pattern.type(), pattern.identifier());
        orEmpty(pattern.conditions())
                .forEach(condition -> patternDescr.addConstraint(new ExprConstraintDescr(condition)));
        return patternDescr;
    }

    private static String toConsequence(List<String> actions) {
        return orEmpty(actions).stream()
                .map(action -> action + STATEMENT_SEPARATOR)
                .collect(Collectors.joining());
    }

    private static <T> List<T> orEmpty(List<T> items) {
        return items == null ? List.of() : items;
    }
}
