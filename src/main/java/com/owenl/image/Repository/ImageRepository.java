package com.owenl.image.Repository;

import com.owenl.image.Model.TableModel.ImageDO;
import com.owenl.image.Model.TableModel.ImageObjDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageDO, Integer> {

    //select i.id as imageId, i.label as imageLabel, i.url, o.id as objectId, o.confidence, o.name from
    //image  as i left outer join
    //object as o
    //on i.id = o.image_Id;
//    @Query("SELECT i.id as imageId, i.label as imageLabel, i.url, o.id as objectId, o.confidence, o.name FROM image as i LEFT OUTER JOIN object as o ON i.id = o.image_Id")
//    List<ImageObjDO> fetchImageObjectLeftOuterJoin();

    @Query("SELECT new com.owenl.image.Model.TableModel.ImageObjDO(i.id, i.label, i.url, o.id, o.confidence, o.name)" +
            "FROM image as i LEFT OUTER JOIN object as o ON i.id = o.image_Id")
    List<ImageObjDO> fetchImageObjectLeftOuterJoin();

    @Query("SELECT new com.owenl.image.Model.TableModel.ImageObjDO(i.id, i.label, i.url, o.id, o.confidence, o.name)" +
            "FROM image as i JOIN object as o ON i.id = o.image_Id WHERE o.name IN (:names)")
    List<ImageObjDO> fetchImagesByObject(@Param("names") List<String> names);

    @Query("SELECT new com.owenl.image.Model.TableModel.ImageObjDO(i.id, i.label, i.url, o.id, o.confidence, o.name)" +
            "FROM image as i LEFT OUTER JOIN object as o ON i.id = o.image_Id WHERE i.id = ?1")
    List<ImageObjDO> fetchImageById(int id);

}
