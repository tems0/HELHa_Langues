package helha.tems.helha_langue.repositories;

import helha.tems.helha_langue.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepo extends JpaRepository<Response,Integer> {
}
