package ai.qodo.cover.plugin

import spock.lang.Specification

class TestInfoResponseSpec extends Specification {
    // Creating an instance with a valid filepath
    def "should create instance with valid filepath"() {
        given:
        String filepath = "/valid/path/to/file.txt"

        when:
        TestInfoResponse response = new TestInfoResponse(filepath)

        then:
        response.filepath() == filepath
    }

}
