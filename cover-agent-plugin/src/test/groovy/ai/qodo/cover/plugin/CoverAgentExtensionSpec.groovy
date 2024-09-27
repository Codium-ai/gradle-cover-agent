package ai.qodo.cover.plugin
import org.gradle.api.provider.Property;
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import spock.lang.Specification

class CoverAgentExtensionSpec extends Specification {
    void setup() {
    }
    // Initializes ai.qodo.cover.plugin.ConcreteCoverAgentExtension with a valid Project object
    def "should initialize with valid Project object"() {
        given:
        Project project = Mock(Project)
        ObjectFactory objectFactory = Mock(ObjectFactory)
        Property<String> stringProperty = Mock(Property<String>)
        Property<Integer> intProperty = Mock(Property<Integer>)

        when:
        ConcreteCoverAgentExtension extension = new ConcreteCoverAgentExtension(project)


        then:
        1 * project.getObjects() >> objectFactory
        3 * objectFactory.property(String.class) >> stringProperty
        2 * objectFactory.property(Integer.class) >> intProperty
        extension.getApiKey() == stringProperty
        extension.getCoverage() == intProperty
        extension.getCoverAgentBinaryPath() == stringProperty
        extension.getIterations() == intProperty
        extension.getWanDBApiKey() == stringProperty
    }

}
