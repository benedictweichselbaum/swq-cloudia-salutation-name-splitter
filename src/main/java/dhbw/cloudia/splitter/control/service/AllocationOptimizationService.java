package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllocationOptimizationService {

    public List<ContactPartAllocation> optimizeAllocation(List<ContactPartAllocation> contactPartAllocations) {
        return contactPartAllocations;
    }
}
