/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  bernamp
 * Created: 22-Apr-2016
 */

set foreign_key_checks = 0;
truncate table code_system;
truncate table code_system_concept;
truncate table code_system_version;
truncate table designation;
truncate table transcoding_association;
truncate table value_set;
truncate table value_set_version;
truncate table x_concept_value_set;
set foreign_key_checks = 1;
