package com.GP.ELsayes.service;

import java.util.List;

public interface CrudService<Request, Entity, Response, ID> {

    public Response add(Request request);

    public Response update(Request request, ID id);

    public void delete(ID id);

    public List<Response> getAll();

    public Entity getById(ID id);
    public Response getResponseById(ID id);


}
