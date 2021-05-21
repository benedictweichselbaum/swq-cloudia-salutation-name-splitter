package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.control.contactstringpart.ContactParts;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import dhbw.cloudia.splitter.control.helper.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * This class contains an example Unit Test for the SalutationService
 */
@ExtendWith(SpringExtension.class)
class SalutationServiceTest {

    @InjectMocks
    SalutationService salutationService;

    @BeforeEach
    void setUp() {
        salutationService = new SalutationService();
    }

    /**
     * This is just an example unit test to show that we know how to write unit tests.
     * The quality assurance for this project was realized with integration tests in Postman.
     * See the Postman Collection in integration-tests in /ContactSplitterIntegrationTests.postman_collection.json for details
     */

    @Test
    void setSexAndLetterSalutationIsCorrectlySetForFrauSandraBergerTest() {
        List<ContactPartAllocation> givenContactPartAllocation = new ArrayList<>();
        ContactDTO givenContactDto = ContactDTO.builder().build();
        givenContactPartAllocation.add(new ContactPartAllocation(new Tuple<>(0, "Frau"), ContactParts.SALUTATION));
        givenContactPartAllocation.add(new ContactPartAllocation(new Tuple<>(2, "Berger"), ContactParts.LAST_NAME));
        givenContactPartAllocation.add(new ContactPartAllocation(new Tuple<>(1, "Sandra"), ContactParts.FIRST_NAME));

        ContactDTO expected = ContactDTO.builder().letterSalutation("Sehr geehrte Frau null null").salutation("Frau").gender("F").build();
        ContactDTO actual = salutationService.setSexAndLetterSalutation(givenContactPartAllocation, givenContactDto);

        assertEquals(expected, actual, "The Contact Frau Sandra Berger was not correctly allocated to the generated contact in terms of sex and letter salutation");
    }
}

