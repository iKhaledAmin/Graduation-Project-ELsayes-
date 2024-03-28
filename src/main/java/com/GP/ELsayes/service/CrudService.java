package com.GP.ELsayes.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<Request, Entity, Response, ID> {

    public Response add(Request request);

    public Response update(Request request, ID id);

    public void delete(ID id);

    public List<Response> getAll();

    public Optional<Entity> getObjectById(ID id);
    public Entity getById(ID id);

    public Response getResponseById(ID id);




}
