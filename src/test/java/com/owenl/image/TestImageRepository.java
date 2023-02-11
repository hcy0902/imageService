package com.owenl.image;

import com.owenl.image.Model.TableModel.ImageDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestImageRepository extends JpaRepository<ImageDO, Integer> {


    List<ImageDO> findByUrl(String url);

}
