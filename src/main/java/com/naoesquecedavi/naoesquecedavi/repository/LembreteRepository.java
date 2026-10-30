package com.naoesquecedavi.naoesquecedavi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.naoesquecedavi.naoesquecedavi.model.Lembrete;
import org.springframework.stereotype.Repository;

@Repository
public interface LembreteRepository extends JpaRepository<Lembrete, Long> {
}
