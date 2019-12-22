package com.study.wjm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.mongodb.client.gridfs.model.GridFSFile;

@Controller
public class FileController {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @GetMapping("/download")
    public String download(String fid) {
        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fid)));
        GridFsResource gridFsResource = gridFsTemplate.getResource(fsFile);
//        gridFsResource.getInputStream();

        return "";
    }
}
