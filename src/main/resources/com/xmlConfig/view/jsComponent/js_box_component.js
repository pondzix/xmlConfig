

window.com_xmlConfig_view_jsComponent_JsBoxComponent = function() {

  this.onStateChange = function() {
	  if(this.getState().sizeChange){
		  var boxItem = boxList[this.getState().boxId];
		  calculateBoxDimensions(this.getState(), boxItem.boxDim);	   
		    var scaleX = boxItem.boxDim.nx / boxItem.defaultBoxDim.nx;
	      	var scaleY = boxItem.boxDim.ny / boxItem.defaultBoxDim.ny;
	      	var scaleZ = boxItem.boxDim.nz / boxItem.defaultBoxDim.nz;
	      	boxItem.box.scale.set(scaleX, scaleY, scaleZ);
	      	boxItem.box.position.x = boxItem.boxDim.posX;
			boxItem.box.position.y = boxItem.boxDim.posY;
			boxItem.box.position.z = boxItem.boxDim.posZ;      	
	  } else {
		  var b = {box : {}, boxDim : {}, defaultBoxDim : {}};
		  boxList[this.getState().boxId] = b;
		  calculateBoxDimensions(this.getState(), boxList[this.getState().boxId].defaultBoxDim);
	      setBoxDimensions(boxList[this.getState().boxId].boxDim, boxList[this.getState().boxId].defaultBoxDim);	 		  
	  }		   
  };
};

function calculateBoxDimensions(state, box){	
	box.dx = (state.dx) ? state.dx : '0';
	box.dy = (state.dy) ? state.dy : '0';
	box.dz = (state.dz) ? state.dz : '0';
	box.nx = (state.nx) ? state.nx : geometryDim.nx - box.dx;
	box.ny = (state.ny) ? state.ny : geometryDim.ny - box.dy;
	box.nz = (state.nz) ? state.nz : geometryDim.nz - box.dz;
    calculateBoxPosition(box); 
}

function calculateBoxPosition(box){
	if(box.dx > 0){
		box.posX = (- geometryDim.nx / 2) + (box.dx / 1) + (box.nx / 2);
	} else {
		box.posX = (geometryDim.nx / 2) + (box.dx / 1) - (box.nx / 2);		
	}
	
	if(box.dy > 0){
		box.posY = (- geometryDim.ny / 2) + (box.dy / 1) + (box.ny / 2);
	} else {
		box.posY = (geometryDim.ny / 2) + (box.dy / 1) - (box.ny / 2);		
	}
	
	if(box.dz > 0){
		 box.posZ = (- geometryDim.nz / 2) + (box.dz / 1) + (box.nz / 2);	
	} else {
		 box.posZ = (geometryDim.nz / 2) + (box.dz / 1) - (box.nz / 2);	
	}
}

function setBoxDimensions(currentDim, defaultDim){
	currentDim.nx = defaultDim.nx;
	currentDim.ny = defaultDim.ny;
	currentDim.nz = defaultDim.nz;
	currentDim.dx = defaultDim.dx;
	currentDim.dy = defaultDim.dy;
	currentDim.dz = defaultDim.dz;	
}