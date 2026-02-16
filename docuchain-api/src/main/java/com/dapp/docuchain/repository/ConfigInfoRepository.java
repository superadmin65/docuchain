package com.dapp.docuchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dapp.docuchain.model.Config;

public interface ConfigInfoRepository extends JpaRepository<Config, Integer> {

	public Config findConfigByConfigKey(String configKey);

}