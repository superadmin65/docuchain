var adminVessels = angular.module('dapp.AdminVesselsController', ['angularUtils.directives.dirPagination']);
adminVessels.controller('AdminVesselsController', ['$scope', '$window', '$location', '$state', '$rootScope', 'FunctionalityService', 'toaster', function ($scope, $window, $location, $state, $rootScope, FunctionalityService, toaster) {
    if (localStorage.length == 0) {
        $location.path('/');
    }
    $scope.vesselsList = [];
    $scope.commercialManagerInfoList = [];
    $scope.techManagerInfoList = [];
    $scope.shipAllList = [];
    $scope.listTech = [];
    $scope.listCom = [];
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.totalTechLength;
    $scope.shipIds = [];

    $scope.userId = $window.localStorage.getItem('userId');
    $scope.maxShipCount = $window.localStorage.getItem('maxShipCount');
    $scope.getId;
    var selecTech;
    var selecShip;
    $scope.loader = false;
    var buttonVessel = document.getElementById("addVesselid");
    var selecCom;

    getList();

    function getList() {
        FunctionalityService.getVessellist($scope.userId).then(function (response) {
            if (response.status == 201 || response.status == 200) {
                $scope.vesselsList = response.data.shipProfileList;
                console.log("vessel response", response)
                angular.forEach($scope.vesselsList, function (value) {
                    $scope.commercialManagerInfoList.push(value.commercialManagerInfoList);
                    $scope.techManagerInfoList.push(value.techManagerInfoList);
                })


                // $scope.vesselsList.map((value, key) => {
                //     $scope.commercialManagerInfoList.push(value.commercialManagerInfoList);
                //     $scope.techManagerInfoList.push(value.techManagerInfoList);
                // })

            }
            else if (response.status == 206) {
                toaster.pop('error', response.data.message);
            }
        })
    }
    $scope.checkVessellimit = function () {
        toaster.clear();
        if ($scope.vesselsList.length >= $scope.maxShipCount) {
            toaster.error("Vessel limit exceeded")
            buttonVessel.disabled = true;
        }
        else {
            buttonVessel.disabled = false;
            $state.go('dapp.adminvesselsAdd');
        }
    }
    $scope.adminvesselsEdit = function (editId, countryName, stateName,customDocumentHolders) {
        $state.go('dapp.adminvesselsEdit');
        $rootScope.editId = editId;
        $rootScope.countryName = countryName;
        $rootScope.allPlaceHolderList = customDocumentHolders; 
        $rootScope.stateName = stateName;
        $window.localStorage.setItem('editId', JSON.stringify($rootScope.editId));
        $window.localStorage.setItem('countryName', JSON.stringify($rootScope.countryName));
        $window.localStorage.setItem('stateName', JSON.stringify($rootScope.stateName));
    }

    $scope.list = {
        vesselsList: []
    };

    $scope.checkAll = function (allList) {
        if ($scope.checkall) {
            $scope.checkall = true;
            $scope.shipAllList = allList;
            $scope.shipIds = [];

        } else {
            $scope.checkall = false;
            $scope.shipIds = [];
        }

        angular.forEach($scope.vesselsList, function (each) {
            each.checked = $scope.checkall;
            $scope.shipIds.push(each.id)
        });
    }

    $scope.updateCheckall = function () {
        var userTotal = $scope.vesselsList.length;
        var count = 0;
        $scope.shipIds = [];
        angular.forEach($scope.vesselsList, function (item) {
            if (item.checked) {
                count++;
                $scope.shipIds.push(item.id);
            }

        });

        if (userTotal == count) {
            $scope.checkall = true;
        } else {
            $scope.checkall = false;
        }
    };
    $scope.singleToggle = function (object) {
        $scope.loader = true;

        toaster.clear();
        $scope.shipIds = [];
        $scope.shipIds.push(object.id);
        var data = {
            "shipIds": $scope.shipIds,
            "userId": $scope.userId
        }

        if (object.status == 1) {
            FunctionalityService.deactive(data).then(function (response) {
                $scope.loader = false;

                if (response.status == 201 || response.status == 200) {
                    getList();
                    toaster.pop('success', 'Vessel profile deactivated successfully');
                    $scope.shipIds = [];
                    $scope.checkall = false;

                }
                else if (response.status == 206) {
                    toaster.pop('error', response.data.message);
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
            });

        }
        else {
            FunctionalityService.active(data).then(function (response) {
                $scope.loader = false;

                if (response.status == 201 || response.status == 200) {
                    getList();
                    toaster.pop('success', 'Vessel profile activated successfully');
                    $scope.shipIds = [];
                    $scope.checkall = false;
                }
                else if (response.status == 206) {
                    toaster.pop('error', response.data.message);
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
            });
        }
    };
    $scope.openDelete = function (id) {
        $scope.shipIds = id;
    }
    $scope.yesDelete = function () {
        $scope.loader = true;

        var data = {
            "id": $scope.shipIds,
            "userId": $scope.userId
        }

        FunctionalityService.deleteOne(data).then(function (response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
                getList();
                toaster.success({ title: 'Vessel profile deleted successfully' });
                setTimeout(function () {
                    $state.reload();
                }, 300)
            }
            else if (response.status == 206) {
                toaster.error({ title: response.data.message });
            }
        });
    }

    $scope.activeAll = function () {
        $scope.loader = true;

        toaster.clear();
        var data = {
            "shipIds": $scope.shipIds,
            "userId": $scope.userId
        }

        FunctionalityService.active(data).then(function (response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
                getList();
                toaster.success({ title: response.data.message });
                $scope.shipIds = [];
                $scope.checkall = false;
            }
            else if (response.status == 206) {
                toaster.error({ title: response.data.message });
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
        });
    }

    $scope.deactiveAll = function () {
        $scope.loader = true;

        toaster.clear();
        var data = {
            "shipIds": $scope.shipIds,
            "userId": $scope.userId
        }
        FunctionalityService.deactive(data).then(function (response) {
            console.log("deactive response", response)
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
                getList();
                toaster.success({ title: response.data.message });
                $scope.shipIds = [];
                $scope.checkall = false;

            }
            else if (response.status == 206) {
                toaster.error({ title: response.data.message });
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
        });
    }

    $scope.deleteAll = function () {
        $scope.loader = true;

        toaster.clear();
        var data = {
            "shipIds": $scope.shipIds,
            "userId": $scope.userId
        }
        FunctionalityService.delete(data).then(function (response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
                getList();
                toaster.success({ title: response.data.message });
                $scope.shipIds = [];
                $scope.checkall = false;
            }
            else if (response.status == 206) {
                toaster.error({ title: response.data.message });
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
        });
    }

    $scope.removeShipMaster = function (index, getId) {

        selecShip = {
            "id": getId,
            "userId": $scope.userId,
            "shipMasterId": index
        }
        // FunctionalityService.delShipMaster(data).then(function (response) {
        //     console.log("tech del response", response)
        //     if (response.status == 201 || response.status == 200) {
        //         getList();
        //         toaster.success({ title: response.data.message });
        //     }
        //     else if (response.status == 206) {
        //         toaster.error({ title: response.data.message });
        //     }
        // });
    }

    $scope.yesRemoveShip = function () {
        $scope.loader = true;

        FunctionalityService.delShipMaster(selecShip).then(function (response) {
            $scope.loader = false;

            console.log("tech del response", response)
            if (response.status == 201 || response.status == 200) {
                getList();
                toaster.success({ title: response.data.message });
            }
            else if (response.status == 206) {
                toaster.error({ title: response.data.message });
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
        });
    }

    $scope.selectedTech = function (index, getId) {
        selecTech = {
            "id": getId,
            "userId": $scope.userId,
            "techManagerIds": [index]
        }

    }

    $scope.yesRemove = function () {
        $scope.loader = true;

        FunctionalityService.delTech(selecTech).then(function (response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
                getList();
                toaster.success({ title: response.data.message });
            }
            else if (response.status == 206) {
                toaster.error({ title: response.data.message });
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
        });
    }


    $scope.techClick = function (listarray, id) {
        $scope.listTech = listarray;
        $scope.getId = id;
    }

    $scope.selectedTechMore = function (index) {
        $scope.loader = true;

        var data = {
            "id": $scope.getId,
            "userId": $scope.userId,
            "techManagerIds": [index]
        }
        FunctionalityService.delTech(data).then(function (response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
                getList();
                $('#techList').modal('hide');
                toaster.pop('success', response.data.message);

            }
            else if (response.status == 206) {
                toaster.pop('error', response.data.message);
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
        });
    }

    $scope.comClick = function (listarray, id) {
        $scope.listCom = listarray;
        $scope.getId = id;
    }

    $scope.selectedCom = function (index, getId) {

        selecCom = {
            "id": getId,
            "userId": $scope.userId,
            "commercialMasterIds": [index]
        }

    }

    $scope.yesRemovecom = function () {
        $scope.loader = true;

        FunctionalityService.delCom(selecCom).then(function (response) {
            $scope.loader = false;

            if (response.status == 201 || response.status == 200) {
                getList();
                toaster.pop('success', response.data.message);
            }
            else if (response.status == 206) {
                toaster.pop('error', response.data.message);
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
        });
    }
    $scope.selectedComMore = function (getComid) {
        $scope.loader = true;

        var data = {
            "id": $scope.getId,
            "userId": $scope.userId,
            "commercialMasterIds": [getComid]
        }
        FunctionalityService.delCom(data).then(function (response) {
            $scope.loader = false;

            console.log("com delete response", response)
            if (response.status == 201 || response.status == 200) {
                getList();
                $('#commercialList').modal('hide');
                toaster.pop('success', response.data.message);
            }
            else if (response.status == 206) {
                toaster.pop('error', response.data.message);
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
        });
    }

    $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo;
    };

    $scope.pageChanged = function () {
        console.log('Page changed to: ' + $scope.currentPage);
    };

    $scope.setItemsPerPage = function (num) {
        $scope.itemsPerPage = num;
        $scope.currentPage = 1; //reset to first page
    }

}]);