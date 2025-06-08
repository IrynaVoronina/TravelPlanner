export interface WeatherRecommendationDTO {
    isSuitable: boolean;
    message: string;
    precipitationProbability: number;
    thunderstormProbability: number;
    description: string;
    cloudCover: string;
}
