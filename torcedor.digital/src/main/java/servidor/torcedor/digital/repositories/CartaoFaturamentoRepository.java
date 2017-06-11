package servidor.torcedor.digital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import servidor.torcedor.digital.models.CartaoFaturamento;

@Repository
public interface CartaoFaturamentoRepository extends JpaRepository<CartaoFaturamento, Long>{

}
