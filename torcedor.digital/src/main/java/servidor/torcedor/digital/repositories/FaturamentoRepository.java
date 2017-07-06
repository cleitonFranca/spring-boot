package servidor.torcedor.digital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import servidor.torcedor.digital.models.Faturamento;

@Repository
public interface FaturamentoRepository extends JpaRepository<Faturamento, Long>{

}
