package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.FreeTrialCodeRequest;
import com.GP.ELsayes.model.dto.FreeTrialCodeResponse;
import com.GP.ELsayes.model.entity.FreeTrialCode;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.mapper.FreeTrialCodeMapper;
import com.GP.ELsayes.repository.FreeTrialCodeRepo;
import com.GP.ELsayes.service.CustomerService;
import com.GP.ELsayes.service.FreeTrialCodeService;
import com.GP.ELsayes.service.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class FreeTrialCodeServiceImpl implements FreeTrialCodeService {
    private final FreeTrialCodeMapper freeTrialCodeMapper;
    private final FreeTrialCodeRepo freeTrialCodeRepo;
    private final WorkerService workerService;


    private final CustomerService customerService;

    private String newCode(Long workerId){
        Random random = new Random();
        String part1 = String.format("%04d", workerId);
        String part2 = String.format("%04d", random.nextInt(10000));
        String part3 = String.format("%04d", random.nextInt(10000));

        return part1 + "-" + part2 + "-" +part3;
    }

    private Long getIdFromCode (String code) {
        String[] parts = code.split("-");
        Long workerId = Long.parseLong(parts[0]);
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

        System.out.println(" We in Update Fun  ffff " + freeTrialCodeRequest);

        System.out.println(" We in Update Fun  iddd " + codeId);

        FreeTrialCode existedCode = this.getById(codeId);
        FreeTrialCode updatedCode = this.freeTrialCodeMapper.toEntity(freeTrialCodeRequest);

        updatedCode.setId(codeId);
        BeanUtils.copyProperties(updatedCode,existedCode);


        Customer customer = customerService.getById(freeTrialCodeRequest.getCustomerId());
        updatedCode.setCustomer(customer);

        System.out.println(" We in Update Fun customer iddd " + updatedCode.getCustomer().getId());

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
    public FreeTrialCode getById(Long codeId) {
        return freeTrialCodeRepo.findById(codeId).orElseThrow(
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

    public FreeTrialCodeResponse generateCode(Long workerId){
        FreeTrialCodeRequest freeTrialCodeRequest = new FreeTrialCodeRequest();
        freeTrialCodeRequest.setWorkerId(workerId);
        return add(freeTrialCodeRequest);
    }


    public FreeTrialCodeResponse applyCode(Long customerId , String code){
        Long workerId = getIdFromCode(code);

        //System.out.println(" The Woker Id :::::: "+workerId);

        FreeTrialCode freeTrialCode = getByWorkerId(workerId);

        //System.out.println(" The Woker Id 222 :::::: "+freeTrialCode.getWorker().getId());


        freeTrialCode.setDateOfUsing(new Date());

        FreeTrialCodeRequest request = freeTrialCodeMapper.toRequest(freeTrialCode);

        System.out.println(" The request :::::: "+request);

        System.out.println(" The Id :::::: "+freeTrialCode.getId());


        return update(request,freeTrialCode.getId());
    }

}
