package helha.tems.helha_langue.repositories;

import helha.tems.helha_langue.models.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepo extends JpaRepository<Sequence,Integer> {
}
