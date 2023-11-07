package com.niuktok.backend.video.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.niuktok.backend.common.def.LogicDeleteEnum;
import com.niuktok.backend.common.def.ResponseStatusType;
import com.niuktok.backend.common.entity.Video;
import com.niuktok.backend.common.entity.VideoPartition;
import com.niuktok.backend.common.exception.NiuktokException;
import com.niuktok.backend.common.mapper.VideoMapper;
import com.niuktok.backend.common.mapper.VideoPartitionMapper;
import com.niuktok.backend.video.service.PartitionService;

@Service
public class PartitionServiceImpl implements PartitionService {
    @Autowired
    private VideoPartitionMapper partitionMapper;

    @Autowired
    private VideoMapper videoMapper;

	@Override
	public List<VideoPartition> getAllPartitions() {
		return partitionMapper.selectAll();
	}

    public Boolean exist(Long partitionID) {
        VideoPartition partition = new VideoPartition();
        partition.setId(partitionID);
        partition.setIsDeleted(LogicDeleteEnum.NOT_DELETED.value());
        return partitionMapper.selectCount(partition) == 1;
    }

	@Override
    @Transactional
	public PageInfo<Video> getPartitionVideos(Long partitionID, Integer pageNo, Integer pageSize, String orderDir) {
        if (!exist(partitionID)) {
            throw new NiuktokException(ResponseStatusType.NOT_EXISTED_PARTITION);
        }
        Page<Video> page = PageHelper.startPage(pageNo, pageSize);
        videoMapper.selectPartitionVideos(partitionID, orderDir);
        return new PageInfo<>(page.getResult());
	}
}
