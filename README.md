# ConvexHull Visualizer API
![Build](https://github.com/prolific-dev/convexhull-visualizer-api/actions/workflows/build.yml/badge.svg)

The ConvexHull Visualizer API is a powerful backend service designed to calculate and visualize convex hulls for a given set of points. It provides efficient algorithms and a RESTful interface for developers to integrate convex hull calculations into their applications.

## Features

- **Efficient Algorithms**: Supports Graham Scan and QuickHull algorithms for 2D and 3D convex hull calculations.
- **RESTful API**: Easy-to-use endpoints for submitting point data and retrieving convex hull results.
- **Visualization Support**: Generates visual representations of convex hulls.
- **Customizable**: Easily extendable for additional features and algorithms.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/prolific-dev/convexhull-visualizer-api.git
   ```
2. Navigate to the project directory:
   ```bash
   cd convexhull-visualizer-api
   ```
3. Build the project:
   ```bash
   ./mvnw clean install
   ```

### Running the Application

To start the application, run:
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Usage

### Example Input

Submit a JSON payload to the `/api/convexhull` endpoint:
```json
{
  "points": [
    {"x": 1, "y": 2},
    {"x": 3, "y": 4},
    {"x": 5, "y": 6}
  ]
}
```

### Example Output

The API will return the convex hull points:
```json
{
  "hull": [
    {"x": 1, "y": 2},
    {"x": 5, "y": 6},
    {"x": 3, "y": 4}
  ]
}
```

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Contact

For questions or support, please contact [support@prolificdev.com](mailto:support@prolificdev.com).