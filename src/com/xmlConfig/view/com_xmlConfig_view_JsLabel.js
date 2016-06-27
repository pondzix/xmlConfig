var geometryDim = {};
var defaultGeometryDim = {};
var defaultBoxDim = {};
var boxDim = {};
var boxSizeChange, geometrySizeChange;
var geometry, box, scaleX, scaleY, scaleZ;


function calculateBoxDimensions(state, box){	
    box.dx = (state.box[3] == '') ? '0' : state.box[3];
    box.dy = (state.box[4] == '') ? '0' : state.box[4];
    box.dz = (state.box[5] == '') ? '0' : state.box[5];
    box.nx = (state.box[0] == '') ? geometryDim.nx - box.dx : state.box[0];
    box.ny = (state.box[1] == '') ? geometryDim.ny - box.dy : state.box[1];
    box.nz = (state.box[2] == '') ? geometryDim.nz - box.dz : state.box[2];
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

function setBoxDimensions(){
	boxDim.nx = defaultBoxDim.nx;
	boxDim.ny = defaultBoxDim.ny;
	boxDim.nz = defaultBoxDim.nz;
	boxDim.dx = defaultBoxDim.dx;
	boxDim.dy = defaultBoxDim.dy;
	boxDim.dz = defaultBoxDim.dz;	
}

com_xmlConfig_view_JsLabel = function() {

  this.onStateChange = function() {
	  
	geometrySizeChange = this.getState().geometrySizeChange;
	boxSizeChange = this.getState().boxSizeChange;
    if(geometrySizeChange){
    	geometryDim.nx = this.getState().geometry[0];
        geometryDim.ny = this.getState().geometry[1];
        geometryDim.nz = this.getState().geometry[2];   
        
    	scaleX = geometryDim.nx / defaultGeometryDim.nx;
    	scaleY = geometryDim.ny / defaultGeometryDim.ny;
    	scaleZ = geometryDim.nz / defaultGeometryDim.nz;
    	
    	geometry.scale.set(scaleX, scaleY, scaleZ);
    	calculateBoxPosition(boxDim);
    	box.position.x = boxDim.posX;
        box.position.y = boxDim.posY;
        box.position.z = boxDim.posZ;    
    } else if(boxSizeChange){
        calculateBoxDimensions(this.getState(), boxDim);
        
        scaleX = boxDim.nx / defaultBoxDim.nx;
      	scaleY = boxDim.ny / defaultBoxDim.ny;
      	scaleZ = boxDim.nz / defaultBoxDim.nz;
      	
        box.scale.set(scaleX, scaleY, scaleZ);
        box.position.x = boxDim.posX;
        box.position.y = boxDim.posY;
        box.position.z = boxDim.posZ;     
    } else {
    	defaultGeometryDim.nx = this.getState().geometry[0];
    	defaultGeometryDim.ny = this.getState().geometry[1];
    	defaultGeometryDim.nz = this.getState().geometry[2];    
    	
    	geometryDim.nx = defaultGeometryDim.nx;
        geometryDim.ny = defaultGeometryDim.ny;
        geometryDim.nz = defaultGeometryDim.nz;   
        
        calculateBoxDimensions(this.getState(), defaultBoxDim);
        setBoxDimensions();
    }
  };
};
