package projo;

import java.util.ArrayList;

public class Object {
	
	private int classID;
	private String className;
	private ArrayList<Float> features;
	
	public Object(String className, ArrayList<Float> features) {
		
//		this.classID=-1;
		this.className=className;
		this.features=features;
		
	}
	
	String getClassName() {
		return this.className;
	}
	
	ArrayList<Float> getFetures() {
		return this.features;
	}
	
	int getFeaturesNumber() {
		return this.features.size();
	}

	int setClassID(int ID) {
		return this.classID=ID;
	}
	
//	bool Database::load(const std::string &fileName)
//	{
//	    clear();
//
//	    std::ifstream file(fileName);
//
//	    if (!file.is_open())
//	    {
//	        return false;
//	    }
//	    std::string line; getline(file, line);
//
//	    size_t pos = line.find_first_of(',');
//	    if (pos == std::string::npos)
//	        return false;
//	    unsigned int classFeaturesNo = std::stoi(line.substr(0, pos));
//
//	    std::string featuresID = line.substr(pos + 1);
//
//	    while (true)
//	    {
//	        pos = featuresID.find_first_of(',');
//	        if (pos != std::string::npos)
//	        {
//	            std::string feature = featuresID.substr(0, pos);
//	            featuresID = featuresID.substr(pos + 1);
//	            unsigned int featureID = std::stof(feature);
//	            featuresIDs.push_back(featureID);
//	        }
//	        else
//	        {
//	            unsigned int featureID = std::stof(featuresID);
//	            featuresIDs.push_back(featureID);
//	            break;
//	        }
//	    }
//
//	    for (std::string line; getline(file, line);)
//	    {
//	        size_t pos = line.find_first_of(',');
//	        if (pos == std::string::npos)
//	        {
//	            // logowanie b³êdu przy odczycie z pliku
//	          continue;
//	        }
//
//	        std::string className = line.substr(0, pos);
//
//	        size_t classNamePos = className.find_first_of(' ');
//	        if (classNamePos != std::string::npos)
//	            className = className.substr(0, classNamePos);
//
//	        std::string features = line.substr(pos+1);
//
//	        std::vector<float> featuresValues;
//
//	        while (true)
//	        {
//
//	            pos = features.find_first_of(',');
//	            if (pos != std::string::npos)
//	            {
//	                std::string feature = features.substr(0, pos);
//	                features = features.substr(pos + 1);
//	                float featureValue = std::stof(feature);
//	                featuresValues.push_back(featureValue);
//	            }
//	            else
//	            {
//	                float featureValue = std::stof(features);
//	                featuresValues.push_back(featureValue);
//	                break;
//	            }
//	        }
//
//	        if(classFeaturesNo == featuresValues.size())
//	        {
//	            if(addObject(Object(className, featuresValues)))
//	            {
//	                // logowanie b³êdu przy dodawaniu obiektu do bazy
//	            }
//	        }
//	        else return false;
//
//	    }
//	    file.close(); // logowanie poprawnego wczytania do bazy
//	    return true;
//	}


}
