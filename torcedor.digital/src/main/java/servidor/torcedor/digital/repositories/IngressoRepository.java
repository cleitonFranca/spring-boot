package servidor.torcedor.digital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import servidor.torcedor.digital.models.Ingresso;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

}
