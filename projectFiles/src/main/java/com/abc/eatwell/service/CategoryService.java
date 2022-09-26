package com.abc.eatwell.service;

import com.abc.eatwell.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {

    public void remove(Long ids);
}
