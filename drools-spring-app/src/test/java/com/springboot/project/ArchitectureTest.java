package com.springboot.project;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

/** Package-by-feature boundaries of the main code (generated sources included, tests excluded). */
@AnalyzeClasses(
        packages = "com.springboot.project",
        importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    private static final String ROOT = "com.springboot.project";
    private static final String BUSINESS_RULE = ROOT + ".businessrule..";
    private static final String COMMON = ROOT + ".common..";
    private static final String GENERATED = "..generated..";

    @ArchTest
    static final ArchRule controllers_should_be_rest_controllers_in_controller_package =
            classes()
                    .that()
                    .haveSimpleNameEndingWith("Controller")
                    .should()
                    .resideInAPackage("..controller..")
                    .andShould()
                    .beAnnotatedWith(RestController.class);

    @ArchTest
    static final ArchRule controllers_should_depend_on_service_interfaces_only =
            noClasses()
                    .that()
                    .resideInAPackage("..controller..")
                    .should()
                    .dependOnClassesThat()
                    .areAnnotatedWith(Service.class)
                    .orShould()
                    .dependOnClassesThat()
                    .resideInAnyPackage("..repository..", "..engine..", "..drl..");

    @ArchTest
    static final ArchRule services_should_implement_an_interface =
            classes()
                    .that()
                    .areAnnotatedWith(Service.class)
                    .should()
                    .resideInAPackage("..service..")
                    .andShould()
                    .implement(
                            DescribedPredicate.describe(
                                    "an I*Service interface",
                                    javaClass ->
                                            javaClass.getSimpleName().startsWith("I")
                                                    && javaClass.getSimpleName().endsWith("Service")));

    @ArchTest
    static final ArchRule repositories_should_only_be_used_by_services =
            classes()
                    .that()
                    .resideInAPackage("..repository..")
                    .should()
                    .onlyHaveDependentClassesThat()
                    .resideInAnyPackage("..repository..", "..service..");

    @ArchTest
    static final ArchRule generated_contract_should_stay_at_the_web_edge =
            noClasses()
                    .that()
                    .resideInAnyPackage(
                            "..service..",
                            "..repository..",
                            "..entity..",
                            "..model..",
                            "..drl..",
                            "..engine..",
                            "..fact..",
                            "..util..",
                            "..exception..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage(GENERATED);

    @ArchTest
    static final ArchRule common_should_not_depend_on_features =
            noClasses()
                    .that()
                    .resideInAPackage(COMMON)
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage(BUSINESS_RULE);

    @ArchTest
    static final ArchRule rule_facing_classes_should_not_depend_on_spring =
            noClasses()
                    .that()
                    .resideInAnyPackage("..businessrule.fact..", "..common.util..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("org.springframework..")
                    .because("rules reference them by FQN and run inside Drools, not Spring");

    @ArchTest
    static final ArchRule models_should_be_records =
            classes().that().resideInAPackage("..businessrule.model..").should().beRecords();

    @ArchTest
    static final ArchRule feature_packages_should_be_free_of_cycles =
            slices().matching(ROOT + ".businessrule.(*)..").should().beFreeOfCycles();

    @ArchTest
    static final ArchRule hand_written_code_should_not_use_standard_streams =
            noClasses().that().resideOutsideOfPackage(GENERATED).should(ACCESS_STANDARD_STREAMS);

    @ArchTest
    static final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    @ArchTest
    static final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
}
