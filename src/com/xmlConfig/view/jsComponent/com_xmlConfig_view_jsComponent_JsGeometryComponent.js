var geometryDim = {};
var defaultGeometryDim = {};
var geometry, box, scaleX, scaleY, scaleZ;
var boxList = {};
var wedgeList = {};

com_xmlConfig_view_jsComponent_JsGeometryComponent = function() {

  this.onStateChange = function() {
	  if(!this.getState().sizeChange){
		  clearComponents();
		  setGeometry(defaultGeometryDim, this.getState()) ;	    	
	      geometryDim.nx = defaultGeometryDim.nx;
	      geometryDim.ny = defaultGeometryDim.ny;
	      geometryDim.nz = defaultGeometryDim.nz;  
	  } else {
		  setGeometry(geometryDim, this.getState()) ;       
	      scaleX = geometryDim.nx / defaultGeometryDim.nx;
	      scaleY = geometryDim.ny / defaultGeometryDim.ny;
	      scaleZ = geometryDim.nz / defaultGeometryDim.nz;	    	
	      geometry.scale.set(scaleX, scaleY, scaleZ);	
	      updateBoxes();
	  }		
  };
};

function clearComponents(){
	geometryDim = {};
	defaultGeometryDim = {};
	boxList = {};
	wedgeList = {};
}
function setGeometry(geometry, state){
	geometry.nx = (state.nx) ? state.nx : '1';
	geometry.ny = (state.ny) ? state.ny : '1';
	geometry.nz = (state.nz) ? state.nz : '1';
}

function updateBoxes(){
	for(var b in window.parent.boxList){
		var boxItem = window.parent.boxList[b];
		calculateBoxPosition(boxItem.boxDim);
		boxItem.box.position.x = boxItem.boxDim.posX;
		boxItem.box.position.y = boxItem.boxDim.posY;
		boxItem.box.position.z = boxItem.boxDim.posZ;
	}
}

function calculateBoxPosition(box){
	if(box.dx >= 0){
		box.posX = (- geometryDim.nx / 2) + (box.dx / 1) + (box.nx / 2);
	} else {
		box.posX = (geometryDim.nx / 2) + (box.dx / 1) - (box.nx / 2);		
	}
	
	if(box.dy >= 0){
		box.posY = (- geometryDim.ny / 2) + (box.dy / 1) + (box.ny / 2);
	} else {
		box.posY = (geometryDim.ny / 2) + (box.dy / 1) - (box.ny / 2);		
	}
	
	if(box.dz >= 0){
		 box.posZ = (- geometryDim.nz / 2) + (box.dz / 1) + (box.nz / 2);	
	} else {
		 box.posZ = (geometryDim.nz / 2) + (box.dz / 1) - (box.nz / 2);	
	}
}