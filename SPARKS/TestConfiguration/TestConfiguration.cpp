#include "stdafx.h"


int main(int argc, char **argv)
{

	try {

		if (argc != 2) { 
			puts("falta el parametro con el fichero de configuracion");
			return -1;
		}

		mainConfig.loadConfiguration(argv[1]);
		

		char s[128];
		mainConfig.getString("Scene.BackgroundPicture", s, 128);
		//printf("pic='%s'\n", s);

		int morphTargetNumber;
		morphTargetNumber = mainConfig.getLength("VisemeMapping.Mapping");
		//printf("morphTargetNumber: %d\n", morphTargetNumber);


		char restLabel[80];
		mainConfig.getString("VisemeMapping.RestLabel", restLabel, 80);
		//printf("VisemeMapping.RestLabel = '%s'\n", restLabel);

	}
	catch (Exception &ex) {
		printf("ERROR\n%s", ex.msg);
		return 1;
	}
}
