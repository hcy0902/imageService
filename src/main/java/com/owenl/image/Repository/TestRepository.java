package com.owenl.image.Repository;

import com.owenl.image.Model.TableModel.ImageDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<ImageDO, Integer> {

    //@Query("SELECT i. FROM image as i WHERE i.url = ?1")
    List<ImageDO> findByUrl(String url);

}
