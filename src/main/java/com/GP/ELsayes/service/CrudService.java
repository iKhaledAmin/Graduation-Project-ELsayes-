package com.GP.ELsayes.service;

import java.util.List;

public interface CrudService<Request, Entity, Response, ID> {

    public Entity getById(ID id);

    public Response add(Request request);

    public List<Response> getAll();

    public Response update(Request request, ID id);

    public void delete(ID id);
}
