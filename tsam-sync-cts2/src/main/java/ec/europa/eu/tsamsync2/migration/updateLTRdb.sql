/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  bernamp
 * Created: 02-May-2016
 */
set foreign_key_checks = 0;
ALTER TABLE `code_system`
	ALTER `ID` DROP DEFAULT;
ALTER TABLE `code_system`
	CHANGE COLUMN `ID` `ID` BIGINT(20) NOT NULL AUTO_INCREMENT FIRST;
ALTER TABLE `code_system_concept`
	CHANGE COLUMN `ID` `ID` BIGINT(20) NOT NULL AUTO_INCREMENT FIRST;
ALTER TABLE `code_system_version`
	CHANGE COLUMN `ID` `ID` BIGINT(20) NOT NULL AUTO_INCREMENT FIRST;
ALTER TABLE `designation`
	CHANGE COLUMN `ID` `ID` BIGINT(20) NOT NULL AUTO_INCREMENT FIRST;
ALTER TABLE `transcoding_association`
	CHANGE COLUMN `ID` `ID` BIGINT(20) NOT NULL AUTO_INCREMENT FIRST;
ALTER TABLE `value_set`
	CHANGE COLUMN `ID` `ID` BIGINT(20) NOT NULL AUTO_INCREMENT FIRST;
ALTER TABLE `value_set_version`
	CHANGE COLUMN `ID` `ID` BIGINT(20) NOT NULL AUTO_INCREMENT FIRST;
set foreign_key_checks = 1;
