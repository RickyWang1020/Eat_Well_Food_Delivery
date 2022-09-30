package com.abc.eatwell.service.impl;

import com.abc.eatwell.entity.AddressBook;
import com.abc.eatwell.mapper.AddressBookMapper;
import com.abc.eatwell.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
