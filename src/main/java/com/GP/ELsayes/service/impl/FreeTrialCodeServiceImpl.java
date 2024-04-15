package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.FreeTrialCodeRequest;
import com.GP.ELsayes.model.dto.FreeTrialCodeResponse;
import com.GP.ELsayes.model.entity.FreeTrialCode;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.ServicesOfBranches;
import com.GP.ELsayes.model.mapper.FreeTrialCodeMapper;
import com.GP.ELsayes.repository.FreeTrialCodeRepo;
import com.GP.ELsayes.service.CustomerService;
import com.GP.ELsayes.service.FreeTrialCodeService;
import com.GP.ELsayes.service.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class FreeTrialCodeServiceImpl implements FreeTrialCodeService {
    private final FreeTrialCodeMapper freeTrialCodeMapper;
    private final FreeTrialCodeRepo freeTrialCodeRepo;
    private final WorkerService workerService;
    private final CustomerService customerService;
    public FreeTrialCodeServiceImpl(
            FreeTrialCodeMapper freeTrialCodeMapper,
            FreeTrialCodeRepo freeTrialCodeRepo,
            @Lazy WorkerService workerService,
            CustomerService customerService) {
        this.freeTrialCodeMapper = freeTrialCodeMapper;
        this.freeTrialCodeRepo = freeTrialCodeRepo;
        this.workerService = workerService;
        this.customerService = customerService;
    }



    private void throwExceptionIfCodeIsUsedBefore(String code){
        Optional<FreeTrialCode> freeTrialCode = freeTrialCodeRepo.findByCode(code);
        if(freeTrialCode.get().getDateOfUsing() == null){
            return;
        }
        throw new RuntimeException("Invalid code");
    }

    private void throwExceptionIfCodeNotExist(String code){
        Optional<FreeTrialCode> freeTrialCode = freeTrialCodeRepo.findByCode(code);
        if(freeTrialCode.isEmpty()){
            throw new RuntimeException("Invalid code");
        }
        return;
    }

    private String newCode(Long workerId){
        Random random = new Random();
        String part1 = String.format("%04d", random.nextInt(10000));
        String part2 = String.format("%04d", workerId);
        String part3 = String.format("%04d", random.nextInt(10000));

        return part1 + "-" + part2 + "-" +part3;
    }

    private Long getIdFromCode (String code) {
        String[] parts = code.split("-");
        Long workerId = Long.parseLong(parts[1]);
        return workerId;
    }


    @Override
    public FreeTrialCodeResponse add(FreeTrialCodeRequest freeTrialCodeRequest) {

        Worker worker = workerService.getById(freeTrialCodeRequest.getWorkerId());
        String stringOfCode = newCode(freeTrialCodeRequest.getWorkerId());

        FreeTrialCode code = this.freeTrialCodeMapper.toEntity(freeTrialCodeRequest);
        code.setWorker(worker);
        code.setCode(stringOfCode);
        code.setDateOfGenerate(new Date());

        return this.freeTrialCodeMapper.toResponse(freeTrialCodeRepo.save(code));
    }


    @SneakyThrows
    @Override
    public FreeTrialCodeResponse update(FreeTrialCodeRequest freeTrialCodeRequest, Long codeId) {

        FreeTrialCode existedCode = this.getById(codeId);
        FreeTrialCode updatedCode = this.freeTrialCodeMapper.toEntity(freeTrialCodeRequest);

        updatedCode.setId(codeId);
        BeanUtils.copyProperties(updatedCode,existedCode);

        return this.freeTrialCodeMapper.toResponse(freeTrialCodeRepo.save(updatedCode));
    }


    @Override
    public void delete(Long codeId) {
        this.getById(codeId);
        freeTrialCodeRepo.deleteById(codeId);
    }

    @Override
    public List<FreeTrialCodeResponse> getAll() {
        return freeTrialCodeRepo.findAll()
                .stream()
                .map(code ->  freeTrialCodeMapper.toResponse(code))
                .toList();
    }

    @Override
    public Optional<FreeTrialCode> getObjectById(Long codeId) {
        return freeTrialCodeRepo.findById(codeId);
    }

    @Override
    public FreeTrialCode getById(Long codeId) {
        return getObjectById(codeId).orElseThrow(
                () -> new NoSuchElementException("Invalid code")
        );
    }

    @Override
    public FreeTrialCodeResponse getResponseById(Long codeId) {
        return freeTrialCodeMapper.toResponse(getById(codeId));
    }



    @Override
    public FreeTrialCode getByWorkerId(Long workerId) {
        return freeTrialCodeRepo.findByWorkerId(workerId).orElseThrow(
                () -> new NoSuchElementException("Invalid code")
        );
    }

    @Override
    public FreeTrialCode getByCode(String code) {
        return freeTrialCodeRepo.findByCode(code).orElseThrow(
                () -> new NoSuchElementException("Invalid code")
        );
    }

    public FreeTrialCodeResponse generateCode(Long workerId){
        FreeTrialCodeRequest freeTrialCodeRequest = new FreeTrialCodeRequest();
        freeTrialCodeRequest.setWorkerId(workerId);
        return add(freeTrialCodeRequest);
    }


    public FreeTrialCodeResponse applyCode(Long customerId , String code){
        throwExceptionIfCodeNotExist(code);
        throwExceptionIfCodeIsUsedBefore(code);

        Customer customer = customerService.getById(customerId);

        FreeTrialCode freeTrialCode = getByCode(code);
        freeTrialCode.setCustomer(customer);
        freeTrialCode.setDateOfUsing(new Date());


        FreeTrialCodeRequest freeTrialCodeRequest = freeTrialCodeMapper.toRequest(freeTrialCode);


        return update(freeTrialCodeRequest,freeTrialCode.getId());
    }

}
