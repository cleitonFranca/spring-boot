package servidor.torcedor.digital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import servidor.torcedor.digital.models.Calendario;

@Repository
public interface CalendarioRepository extends JpaRepository<Calendario, Long > {

}
