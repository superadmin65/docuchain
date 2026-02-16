var userVesselController = angular.module('dapp.UserVesselDcoumentController', []);

userVesselController.controller('UserVesselDcoumentController', ['$scope', '$window', '$location', '$state', '$rootScope', 'FunctionalityService', function ($scope, $window, $location, $state, $rootScope, FunctionalityService) {

    $scope.sessionObject = JSON.parse($window.localStorage.getItem('sessionObject'));

    $scope.loader = false;

    $scope.vesselList = [];
    $scope.currentPage = 1;
    $scope.vesselListLength = $scope.vesselList.length;
	$rootScope.selected = 1;
    // This method is load allShipList based on User
    $scope.getAllShipList = function () {
        $scope.loader = true;

        var data = {
            "userId": $scope.sessionObject.userId,
            "roleId": $scope.sessionObject.roleId
        };
        FunctionalityService.getVesselProfileList(data)
            .then(function (response) {
                $scope.loader = false;

                if (response.status == 200) {
                    $scope.vesselList = response.data.shipProfileList;
                    $scope.vesselListLength = $scope.vesselList.length;
                    console.log("response.data.shipProfileList::"+JSON.stringify(response.data.shipProfileList));
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
    }

    //This method is used to open EXB page using Ship ID
    $scope.openEBDState = function (vessel) {
  

        $window.localStorage.removeItem('libShipId');
        $window.localStorage.removeItem('libshipName');
        $window.localStorage.setItem('libShipId', vessel.id);
        $window.localStorage.setItem('libshipName', vessel.shipName);
        $state.go('dapp.userVesselDocumentEBD');
        
    }


}]);