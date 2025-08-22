package com.example.service;

import com.example.domain.Piesa;
import com.example.repository.IRepository;
import com.example.repository.RepositoryException;

public class PiesaService {
    IRepository<Piesa> piesaRepo;

    public PiesaService(IRepository<Piesa> piesaRepo) {
        // NOTE Injectam dependenta la repository prin constructor
        this.piesaRepo = piesaRepo;
    }

    public void addPiesa(int id, String formatie, String titlu, String gen, String durata) throws RepositoryException {
        Piesa pie = new Piesa(id, formatie,titlu,gen,durata);
        // TODO Validari!?
        piesaRepo.add(pie);
    }

}
