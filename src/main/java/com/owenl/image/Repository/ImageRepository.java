package com.owenl.image.Repository;

import com.owenl.image.Model.TableModel.ImageDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageDO, Integer> {



}
