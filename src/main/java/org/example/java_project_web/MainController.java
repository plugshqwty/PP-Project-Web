package org.example.java_project_web;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.MathEvaluator;
import org.example.MathEvaluatorLibrary;
import org.example.MathEvaluatorNot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.yaml.snakeyaml.Yaml;

import javax.crypto.SecretKey;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        return "index"; // ваш основной шаблон
    }
    List <String> expressions = new ArrayList<>();
    FileProcessing processor = new FileProcessing();
    FileArchiver archiver = new FileArchiver();
    SecretKey key = null;


    @GetMapping("/read/txt")
    public String readTxt(Model model) {
        Path inputPath = Path.of("input.txt");
        try {
            expressions = Files.readAllLines(inputPath);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "read-file";// возвращаем представление для отображения данных
    }

    @GetMapping("/read/xml")
    public String readXml(Model model) {
        Path inputPath = Path.of("input.xml");
        MathExamples mathExamples = null;

        // Очищаем список перед добавлением новых выражений
        expressions.clear();

        try {
            mathExamples = processor.readXmlFile(inputPath);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }

        for (MathExample example : mathExamples.getExamples()) {
            expressions.add(example.getExpression());
        }

        model.addAttribute("expressions", expressions);
        return "read-file"; // возвращаем представление для отображения данных
    }


    @GetMapping("/read/json")
    public String readJson(Model model) {
        Path inputPath = Path.of("input.json"); // Путь к вашему JSON файлу
        expressions.clear();
        try {
            expressions = processor.readJsonFile(inputPath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении JSON файла", e);
        }

        model.addAttribute("expressions", expressions);
        return "read-file"; // Возвращаем представление для отображения данных
    }

    @GetMapping("/read/yaml")
    public String readYaml(Model model) {
        Path inputPath = Path.of("input.yaml");
        expressions.clear();
        List<MathExample> mathExamples = null;
        try {
            mathExamples = processor.readYamlFile(inputPath);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        for (MathExample example : mathExamples) {
            expressions.add(example.getExpression());
        }
        model.addAttribute("expressions", expressions);
        return "read-file"; // возвращаем представление для отображения данных
    }

    @GetMapping("/read/txt/ar")
    public String readTxtAr(Model model) {
        Path archivePath = Path.of("input.zip"); // Путь к архиву всегда фиксированный
        Path outputDir = Paths.get("."); // Текущая директория

        // Разархивируем архив
        try {
            archiver.extractFile(archivePath, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path inputPath = outputDir.resolve("input.txt" );
        try {
            expressions = Files.readAllLines(inputPath);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "read-file";// возвращаем представление для отображения данных
    }

    @GetMapping("/read/xml/ar")
    public String readXmlAr(Model model) {
        Path archivePath = Path.of("input.zip"); // Путь к архиву всегда фиксированный
        Path outputDir = Paths.get("."); // Текущая директория

        // Разархивируем архив
        try {
            archiver.extractFile(archivePath, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path inputPath = outputDir.resolve("input.xml" );
        MathExamples mathExamples = null;

        // Очищаем список перед добавлением новых выражений
        expressions.clear();

        try {
            mathExamples = processor.readXmlFile(inputPath);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }

        for (MathExample example : mathExamples.getExamples()) {
            expressions.add(example.getExpression());
        }

        model.addAttribute("expressions", expressions);
        return "read-file"; // возвращаем представление для отображения данных
    }


    @GetMapping("/read/json/ar")
    public String readJsonAr(Model model) {
        Path archivePath = Path.of("input.zip"); // Путь к архиву всегда фиксированный
        Path outputDir = Paths.get("."); // Текущая директория

        // Разархивируем архив
        try {
            archiver.extractFile(archivePath, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path inputPath = outputDir.resolve("input.json" );// Путь к вашему JSON файлу
        expressions.clear();
        try {
            expressions = processor.readJsonFile(inputPath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении JSON файла", e);
        }

        model.addAttribute("expressions", expressions);
        return "read-file"; // Возвращаем представление для отображения данных
    }

    @GetMapping("/read/yaml/ar")
    public String readYamlAr(Model model) {
        Path archivePath = Path.of("input.zip"); // Путь к архиву всегда фиксированный
        Path outputDir = Paths.get("."); // Текущая директория

        // Разархивируем архив
        try {
            archiver.extractFile(archivePath, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path inputPath = outputDir.resolve("input.yaml" );
        expressions.clear();
        List<MathExample> mathExamples = null;
        try {
            mathExamples = processor.readYamlFile(inputPath);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        for (MathExample example : mathExamples) {
            expressions.add(example.getExpression());
        }
        model.addAttribute("expressions", expressions);
        return "read-file"; // возвращаем представление для отображения данных
    }
    Path encryptedPath;
    Path keyPath;
    Path ivPath;
    @GetMapping("/read/txt/ar/enc")
    public String readTxtArEnc(Model model) {
        Path archivePath = Path.of("input.zip"); // Путь к архиву всегда фиксированный
        Path outputDir = Paths.get("."); // Текущая директория

        // Разархивируем архив
        try {
            archiver.extractFile(archivePath, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        encryptedPath = outputDir.resolve("input.txt.enc");
        keyPath = Paths.get("keyfiletxt.key");
        ivPath = Paths.get("ivfiletxt.bin");
        try {
            SecretKey secretKey = FileEncryptor.loadKey(keyPath); // Изменяем имя переменной
            byte[] iv = FileEncryptor.loadIv(ivPath);
            // Расшифровка файла
            Path decryptedPath = outputDir.resolve("decrypted_output.txt"); // Путь для сохранения расшифрованного файла
            FileEncryptor.decryptFile(encryptedPath, decryptedPath, secretKey, iv);

            // Чтение расшифрованного содержимого
            String decryptedContent = new String(Files.readAllBytes(decryptedPath));
            expressions = extractMathExpressions(decryptedContent, 1); // Передаем '0' или нужный тип

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "read-file";// возвращаем представление для отображения данных
    }
    @GetMapping("/read/xml/ar/enc")
    public String readXmlArEnc(Model model) {
        Path archivePath = Path.of("input.zip"); // Путь к архиву всегда фиксированный
        Path outputDir = Paths.get("."); // Текущая директория

        // Разархивируем архив
        try {
            archiver.extractFile(archivePath, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        encryptedPath = outputDir.resolve("input.xml.enc");
        keyPath = Paths.get("keyfilexml.key");
        ivPath = Paths.get("ivfilexml.bin");
        try {
            SecretKey secretKey = FileEncryptor.loadKey(keyPath); // Изменяем имя переменной
            byte[] iv = FileEncryptor.loadIv(ivPath);
            // Расшифровка файла
            Path decryptedPath = outputDir.resolve("decrypted_output.txt"); // Путь для сохранения расшифрованного файла
            FileEncryptor.decryptFile(encryptedPath, decryptedPath, secretKey, iv);

            // Чтение расшифрованного содержимого
            String decryptedContent = new String(Files.readAllBytes(decryptedPath));
            expressions = extractMathExpressions(decryptedContent, 2); // Передаем '0' или нужный тип

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "read-file";// возвращаем представление для отображения данных
    }
    @GetMapping("/read/json/ar/enc")
    public String readJsonArEnc(Model model) {
        Path archivePath = Path.of("input.zip"); // Путь к архиву всегда фиксированный
        Path outputDir = Paths.get("."); // Текущая директория

        // Разархивируем архив
        try {
            archiver.extractFile(archivePath, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        encryptedPath = outputDir.resolve("input.json.enc");
        keyPath = Paths.get("keyfilejson.key");
        ivPath = Paths.get("ivfilejson.bin");
        try {
            SecretKey secretKey = FileEncryptor.loadKey(keyPath); // Изменяем имя переменной
            byte[] iv = FileEncryptor.loadIv(ivPath);
            // Расшифровка файла
            Path decryptedPath = outputDir.resolve("decrypted_output.txt"); // Путь для сохранения расшифрованного файла
            FileEncryptor.decryptFile(encryptedPath, decryptedPath, secretKey, iv);

            // Чтение расшифрованного содержимого
            String decryptedContent = new String(Files.readAllBytes(decryptedPath));
            expressions = extractMathExpressions(decryptedContent, 3); // Передаем '0' или нужный тип

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "read-file";// возвращаем представление для отображения данных
    }
    @GetMapping("/read/yaml/ar/enc")
    public String readYamlArEnc(Model model) {
        Path archivePath = Path.of("input.zip"); // Путь к архиву всегда фиксированный
        Path outputDir = Paths.get("."); // Текущая директория

        // Разархивируем архив
        try {
            archiver.extractFile(archivePath, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        encryptedPath = outputDir.resolve("input.yaml.enc");
        keyPath = Paths.get("keyfileyaml.key");
        ivPath = Paths.get("ivfileyaml.bin");
        try {
            SecretKey secretKey = FileEncryptor.loadKey(keyPath); // Изменяем имя переменной
            byte[] iv = FileEncryptor.loadIv(ivPath);
            // Расшифровка файла
            Path decryptedPath = outputDir.resolve("decrypted_output.txt"); // Путь для сохранения расшифрованного файла
            FileEncryptor.decryptFile(encryptedPath, decryptedPath, secretKey, iv);

            // Чтение расшифрованного содержимого
            String decryptedContent = new String(Files.readAllBytes(decryptedPath));
            expressions = extractMathExpressions(decryptedContent, 4); // Передаем '0' или нужный тип

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "read-file";// возвращаем представление для отображения данных
    }

    @GetMapping("/read/txt/enc")
    public String readTxtEnc(Model model) {

        encryptedPath = Paths.get("input.txt.enc");
        keyPath = Paths.get("keyfiletxt.key");
        ivPath = Paths.get("ivfiletxt.bin");
        try {
            SecretKey secretKey = FileEncryptor.loadKey(keyPath); // Изменяем имя переменной
            byte[] iv = FileEncryptor.loadIv(ivPath);
            // Расшифровка файла
            Path decryptedPath = Paths.get("decrypted_output.txt"); // Путь для сохранения расшифрованного файла
            FileEncryptor.decryptFile(encryptedPath, decryptedPath, secretKey, iv);

            // Чтение расшифрованного содержимого
            String decryptedContent = new String(Files.readAllBytes(decryptedPath));
            expressions = extractMathExpressions(decryptedContent, 1); // Передаем '0' или нужный тип

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "read-file";// возвращаем представление для отображения данных
    }
    @GetMapping("/read/xml/enc")
    public String readXmlEnc(Model model) {
        encryptedPath = Paths.get("input.xml.enc");
        keyPath = Paths.get("keyfilexml.key");
        ivPath = Paths.get("ivfilexml.bin");
        try {
            SecretKey secretKey = FileEncryptor.loadKey(keyPath); // Изменяем имя переменной
            byte[] iv = FileEncryptor.loadIv(ivPath);
            // Расшифровка файла
            Path decryptedPath = Paths.get("decrypted_output.txt"); // Путь для сохранения расшифрованного файла
            FileEncryptor.decryptFile(encryptedPath, decryptedPath, secretKey, iv);

            // Чтение расшифрованного содержимого
            String decryptedContent = new String(Files.readAllBytes(decryptedPath));
            expressions = extractMathExpressions(decryptedContent, 2); // Передаем '0' или нужный тип

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "read-file";// возвращаем представление для отображения данных
    }
    @GetMapping("/read/json/enc")
    public String readJsonEnc(Model model) {

        encryptedPath = Paths.get("input.json.enc");
        keyPath = Paths.get("keyfilejson.key");
        ivPath = Paths.get("ivfilejson.bin");
        try {
            SecretKey secretKey = FileEncryptor.loadKey(keyPath); // Изменяем имя переменной
            byte[] iv = FileEncryptor.loadIv(ivPath);
            // Расшифровка файла
            Path decryptedPath = Paths.get("decrypted_output.txt"); // Путь для сохранения расшифрованного файла
            FileEncryptor.decryptFile(encryptedPath, decryptedPath, secretKey, iv);

            // Чтение расшифрованного содержимого
            String decryptedContent = new String(Files.readAllBytes(decryptedPath));
            expressions = extractMathExpressions(decryptedContent, 3); // Передаем '0' или нужный тип

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "read-file";// возвращаем представление для отображения данных
    }
    @GetMapping("/read/yaml/enc")
    public String readYamlEnc(Model model) {
        encryptedPath = Paths.get("input.yaml.enc");
        keyPath = Paths.get("keyfileyaml.key");
        ivPath = Paths.get("ivfileyaml.bin");
        try {
            SecretKey secretKey = FileEncryptor.loadKey(keyPath); // Изменяем имя переменной
            byte[] iv = FileEncryptor.loadIv(ivPath);
            // Расшифровка файла
            Path decryptedPath = Paths.get("decrypted_output.txt"); // Путь для сохранения расшифрованного файла
            FileEncryptor.decryptFile(encryptedPath, decryptedPath, secretKey, iv);

            // Чтение расшифрованного содержимого
            String decryptedContent = new String(Files.readAllBytes(decryptedPath));
            expressions = extractMathExpressions(decryptedContent, 4); // Передаем '0' или нужный тип

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "read-file";// возвращаем представление для отображения данных
    }
    @GetMapping("/countR")
    public String countR(Model model) {
        expressions = MathEvaluator.evaluateExpressions(expressions);
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "Counting";// возвращаем представление для отображения данных
    }
    @GetMapping("/countW")
    public String countW(Model model) {
        expressions = MathEvaluatorNot.evaluateExpressions(expressions);
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "Counting";// возвращаем представление для отображения данных
    }
    @GetMapping("/countL")
    public String countL(Model model) {
        expressions = MathEvaluatorLibrary.evaluateExpressions(expressions);
        model.addAttribute("expressions", expressions); // Передаем список в модель
        return "Counting";// возвращаем представление для отображения данных
    }











    // Метод для извлечения математических выражений в зависимости от формата
    private static List<String> extractMathExpressions(String content, int fileType) {
        List<String> mathExpressions = new ArrayList<>();
        switch (fileType) {
            case 2: // XML
                mathExpressions = extractFromXml(content);
                break;
            case 3: // JSON
                mathExpressions = extractFromJson(content);
                break;
            case 4: // YAML
                mathExpressions = extractFromYaml(content);
                break;
            default:
                String[] lines = content.split("\\r?\\n");
                for (String line : lines) {
                    if (isValidMathExpression(line)) {
                        mathExpressions.add(line.trim());
                    }
                }
                break;
        }
        return mathExpressions;
    }

    // Извлечение выражений из XML
    private static List<String> extractFromXml(String xmlContent) {
        List<String> expressions = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlContent.getBytes()));
            NodeList nodeList = document.getElementsByTagName("expression");
            for (int i = 0; i < nodeList.getLength(); i++) {
                expressions.add(nodeList.item(i).getTextContent().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expressions;
    }

    // Извлечение выражений из JSON
    private static List<String> extractFromJson(String jsonContent) {
        List<String> expressions = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonContent);
            JsonNode examplesNode = rootNode.get("examples");

            if (examplesNode != null && examplesNode.isArray()) {
                for (JsonNode node : examplesNode) {
                    JsonNode expressionNode = node.get("expression");
                    if (expressionNode != null) {
                        String expression = expressionNode.asText().trim();
                        if (isValidMathExpression(expression)) {
                            expressions.add(expression);
                        } else {
                            System.out.println("Найдено невалидное математическое выражение: " + expression);
                        }
                    } else {
                        System.out.println("Ключ 'expression' отсутствует в одном из объектов.");
                    }
                }
            } else {
                System.out.println("Ключ 'examples' отсутствует или не является массивом.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при извлечении из JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return expressions;
    }
    // Извлечение выражений из YAML
    private static List<String> extractFromYaml(String yamlContent) {
        List<String> expressions = new ArrayList<>();
        Yaml yaml = new Yaml();

        // Загружаем данные как List<Map<String, String>>
        List<Map<String, String>> data = yaml.load(yamlContent);

        for (Map<String, String> entry : data) {
            String expression = entry.get("expression");
            if (expression != null) {
                expressions.add(expression.trim());
            } else {
                System.out.println("Ключ 'expression' отсутствует в одном из объектов.");
            }
        }
        return expressions;
    }
    // Метод для проверки, является ли строка математическим выражением
    private static boolean isValidMathExpression(String expr) {
        return expr.matches(".*[0-9].*"); // Пример проверки: содержит цифры
    }

}
