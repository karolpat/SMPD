package projo;

import java.util.ArrayList;

public class Object {
	
	private String className;
	private ArrayList<Double> features;
	
	public Object(String className, ArrayList<Double> features) {
		
		this.className=className;
		this.features=features;
		
	}
	
	String getClassName() {
		return this.className;
	}
	
	ArrayList<Double> getFetures() {
		return this.features;
	}
	
	int getFeaturesNumber() {
		return this.features.size();
	}

}

//void MainWindow::on_FSpushButtonCompute_clicked()
//{
//    int dimension = ui->FScomboBox->currentText().toInt();
//
//
//    if( ui->FSradioButtonFisher ->isChecked())
//    {
//    if (dimension == 1 && database.getNoClass() == 2)
//        {
//            float FLD = 0, tmp;
//            int max_ind = -1;
//
//            //std::map<std::string, int> classNames = database.getClassNames();
//            for (uint i = 0; i < database.getNoFeatures(); ++i)
//            {
//                std::map<std::string, float> classAverages;
//                std::map<std::string, float> classStds;
//
//                for (auto const &ob : database.getObjects())
//                {
//                    classAverages[ob.getClassName()] += ob.getFeatures()[i];
//                    classStds[ob.getClassName()] += ob.getFeatures()[i] * ob.getFeatures()[i];
//                }
//
//                std::for_each(database.getClassCounters().begin(), database.getClassCounters().end(), [&](const std::pair<std::string, int> &it)
//                {
//                    classAverages[it.first] /= it.second;
//                    classStds[it.first] = std::sqrt(classStds[it.first] / it.second - classAverages[it.first] * classAverages[it.first]);
//                }
//                );
//
//                tmp = std::abs(classAverages[ database.getClassNames()[0] ] - classAverages[database.getClassNames()[1]]) / (classStds[database.getClassNames()[0]] + classStds[database.getClassNames()[1]]);
//
//                if (tmp > FLD)
//                {
//                    FLD = tmp;
//                    max_ind = i;
//                }
//
//              }
//
//            ui->FStextBrowserDatabaseInfo->append("max_ind: "  +  QString::number(max_ind) + " " + QString::number(FLD));
//
//        }
//        else if(dimension > 1 && database.getNoClass() == 2)
//        {
//            std::vector<float> features;
//            std::vector<Object> objects = database.getObjects();
//            auto matrix = new float[dimension][8];
//            int row=0;
//            int col=0;
//
//            std::vector<Object>::iterator objIt;
//            std::vector<float>::iterator feaIt;
//
//            for(objIt = objects.begin(); objIt != objects.end(); ++objIt) {
//                Object obj = *objIt;
//                features = obj.getFeatures();
//                col++;
//                row=0;
//
//                while(row < dimension){
//
//                    for(feaIt = features.begin(); feaIt != features.end(); ++feaIt){
//                        matrix[row][col] = *feaIt;
//                        QTextStream(stdout) << matrix[row][col];
//                        row++;
//
//                    }
//                }
                /*for(int i=0; i<dimension; i++){
                    for(int j=0; j<8; j++){
                        QTextStream(stdout) << matrix[i][j] << " + " << i << ", " << j;
                        QTextStream(stdout) << " " << endl;
                    }
                }*/

//            }
//
//        }
//     }
