package servidor.torcedor.digital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import servidor.torcedor.digital.models.Rank;


public interface RankRepository extends JpaRepository<Rank, Long>{

}
