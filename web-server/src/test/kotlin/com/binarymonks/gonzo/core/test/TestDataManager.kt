package com.binarymonks.gonzo.core.test

import com.binarymonks.gonzo.core.blog.persistence.BlogRepo
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class TestDataManager {

    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var blogRepo: BlogRepo

    fun clearData() {
        blogRepo.deleteAll()
        userRepo.deleteAll()
    }

}