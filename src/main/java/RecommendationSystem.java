import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class RecommendationSystem {

    public static void main(String[] args) {
        try {
            // Load the data model from a CSV file
            DataModel model = new FileDataModel(new File("ai-based-recommendation-system\\data\\dataset.csv"));

            // Debug: Print the number of users and items
            System.out.println("Number of users: " + model.getNumUsers());
            System.out.println("Number of items: " + model.getNumItems());

            // Calculate user similarity
            UserSimilarity similarity = new LogLikelihoodSimilarity(model);

            // Define the user neighborhood (e.g., consider the top 5 most similar users)
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, model);

            // Debug: Print the neighborhood for user 1
            long userId = 1;
            long[] neighborhoodUsers = neighborhood.getUserNeighborhood(userId);
            System.out.println("User Neighborhood for user " + userId + ": " + Arrays.toString(neighborhoodUsers));

            // Debug: Print items rated by users in the neighborhood
            for (long neighbor : neighborhoodUsers) {
                System.out.println("Items rated by user " + neighbor + ": " + model.getItemIDsFromUser(neighbor));
            }

            // Debug: Print all items in the dataset
            System.out.println("All items in the dataset: " + model.getItemIDs());

            // Create a recommender
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Recommend items for a specific user (user ID 1)
            List<RecommendedItem> recommendations = recommender.recommend(userId, 3); // Reduced to 3

            // Print the recommendations
            if (recommendations.isEmpty()) {
                System.out.println("No recommendations found for user ID: " + userId);
            } else {
                System.out.println("Recommendations for user " + userId + ":");
                for (RecommendedItem recommendation : recommendations) {
                    System.out.println("Item ID: " + recommendation.getItemID() + ", Value: " + recommendation.getValue());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}