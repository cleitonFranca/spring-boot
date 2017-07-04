package servidor.torcedor.digital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import servidor.torcedor.digital.models.Imagem;

public interface ImagemRepository extends JpaRepository<Imagem, Long>  {

}
