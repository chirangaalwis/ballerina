/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/

package org.ballerinalang.repository;

import org.ballerinalang.model.elements.PackageID;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents a composite package source repository, which encapsulates a list
 * of already available {@link PackageSourceRepository}s.
 */
public class CompositePackageSourceRepository extends UserPackageSourceRepository {

    private List<PackageSourceRepository> repos = new ArrayList<>();
    
    public CompositePackageSourceRepository(PackageSourceRepository systemRepo, 
            PackageSourceRepository parentRepo) {
        super(systemRepo, parentRepo);
    }
    
    public void addPackageSourceRepository(PackageSourceRepository repo) {
        this.repos.add(repo);
    }
    
    @Override
    public PackageSource lookupPackageSource(PackageID pkgID) {
        PackageSource result = null;
        for (PackageSourceRepository repo : this.repos) {
            result = repo.getPackageSource(pkgID);
            if (result != null) {
                break;
            }
        }
        return result;        
    }

}